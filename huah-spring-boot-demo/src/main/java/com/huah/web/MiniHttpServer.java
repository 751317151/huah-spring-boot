package com.huah.web;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class MiniHttpServer {
    // 缓存静态资源（使用LRU缓存策略）
    private static final int MAX_CACHE_SIZE = 100;
    private static final ConcurrentLinkedHashMap<String, byte[]> FILE_CACHE =
            new ConcurrentLinkedHashMap.Builder<String, byte[]>()
                    .maximumWeightedCapacity(MAX_CACHE_SIZE)
                    .build();

    // 线程池（动态调整大小）
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(
            4, // 核心线程数
            Runtime.getRuntime().availableProcessors() * 4, // 最大线程数
            60L, TimeUnit.SECONDS, // 空闲线程存活时间
            new LinkedBlockingQueue<>(1000), // 任务队列
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
    );

    // 资源基础路径
    private static final Path RESOURCE_BASE;

    static {
        try {
            URL resourceUrl = MiniHttpServer.class.getClassLoader().getResource("");
            if (resourceUrl == null) {
                throw new RuntimeException("无法获取资源基础路径");
            }
            RESOURCE_BASE = Paths.get(resourceUrl.toURI()).resolve("");
        } catch (URISyntaxException | NullPointerException e) {
            throw new RuntimeException("初始化资源基础路径失败", e);
        }
    }

    // 监控统计
    private static final AtomicLong TOTAL_REQUESTS = new AtomicLong(0);
    private static final AtomicLong CACHE_HITS = new AtomicLong(0);

    public static void main(String[] args) {
        // 确保资源目录存在
        if (!Files.exists(RESOURCE_BASE) || !Files.isDirectory(RESOURCE_BASE)) {
            System.err.println("资源目录不存在: " + RESOURCE_BASE);
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started at http://localhost:8080");
            System.out.println("Resource base: " + RESOURCE_BASE.toAbsolutePath());

            // 添加关闭钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                shutdown();
                System.out.println("\nServer stopped gracefully");
                printStatistics();
            }));

            while (true) {
                Socket client = serverSocket.accept();
                THREAD_POOL.execute(() -> handleRequest(client));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleRequest(Socket client) {
        TOTAL_REQUESTS.incrementAndGet();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             OutputStream out = client.getOutputStream()) {

            // 解析请求行
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            String[] parts = requestLine.split(" ");
            if (parts.length < 2) {
                sendErrorResponse(out, 400, "Bad Request");
                return;
            }

            String method = parts[0];
            String path = parts[1];

            // 只处理GET请求
            if (!"GET".equalsIgnoreCase(method)) {
                sendErrorResponse(out, 405, "Method Not Allowed");
                return;
            }

            // 处理路径：默认为index.html
            if ("/".equals(path)) {
                path = "/index.html";
            }

            // 解析文件路径并防止路径遍历攻击
            String relativePath = path.substring(1);
            Path filePath = RESOURCE_BASE.resolve(relativePath).normalize();

            // 安全检查
            if (!filePath.startsWith(RESOURCE_BASE)) {
                sendErrorResponse(out, 403, "Forbidden");
                return;
            }

            // 检查文件是否存在
            if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
                sendErrorResponse(out, 404, "Not Found");
                return;
            }

            // 尝试从缓存获取
            String cacheKey = filePath.toString();
            byte[] content = FILE_CACHE.get(cacheKey);

            if (content != null) {
                CACHE_HITS.incrementAndGet();
            } else {
                // 读取文件
                content = Files.readAllBytes(filePath);
                // 更新缓存
                FILE_CACHE.put(cacheKey, content);
            }

            // 发送响应
            String contentType = getContentType(filePath);
            sendOkResponse(out, contentType, content);

        } catch (IOException e) {
            System.err.println("Request handling error: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }

    private static void sendOkResponse(OutputStream out, String contentType, byte[] content) throws IOException {
        String header = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + content.length + "\r\n" +
                "Connection: close\r\n\r\n";
        out.write(header.getBytes(StandardCharsets.UTF_8));
        out.write(content);
        out.flush();
    }

    private static void sendErrorResponse(OutputStream out, int code, String message) throws IOException {
        String responseBody = "Error " + code + ": " + message;
        String header = "HTTP/1.1 " + code + " " + message + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + responseBody.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "Connection: close\r\n\r\n";

        out.write(header.getBytes(StandardCharsets.UTF_8));
        out.write(responseBody.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    private static String getContentType(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();

        if (fileName.endsWith(".html")) return "text/html; charset=utf-8";
        if (fileName.endsWith(".css")) return "text/css; charset=utf-8";
        if (fileName.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".ico")) return "image/x-icon";
        if (fileName.endsWith(".svg")) return "image/svg+xml";
        if (fileName.endsWith(".txt")) return "text/plain; charset=utf-8";

        return "application/octet-stream";
    }

    private static void shutdown() {
        // 关闭线程池
        THREAD_POOL.shutdown();
        try {
            if (!THREAD_POOL.awaitTermination(5, TimeUnit.SECONDS)) {
                THREAD_POOL.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 清空缓存
        FILE_CACHE.clear();
    }

    private static void printStatistics() {
        System.out.println("\n=== Server Statistics ===");
        System.out.println("Total requests: " + TOTAL_REQUESTS.get());
        System.out.println("Cache hits: " + CACHE_HITS.get());
        System.out.printf("Cache hit ratio: %.2f%%\n",
                (TOTAL_REQUESTS.get() > 0 ?
                        (CACHE_HITS.get() * 100.0 / TOTAL_REQUESTS.get()) : 0));
        System.out.println("Cache size: " + FILE_CACHE.size());
    }

    // 简单的LRU缓存实现
    private static class ConcurrentLinkedHashMap<K, V> extends ConcurrentHashMap<K, V> {
        private final int maxSize;
        private final LinkedBlockingDeque<K> accessQueue = new LinkedBlockingDeque<>();

        public ConcurrentLinkedHashMap(int maxSize) {
            super();
            this.maxSize = maxSize;
        }

        @Override
        public V put(K key, V value) {
            synchronized (accessQueue) {
                // 如果达到最大大小，移除最旧的条目
                if (size() >= maxSize && !containsKey(key)) {
                    K oldest = accessQueue.poll();
                    if (oldest != null) {
                        super.remove(oldest);
                    }
                }

                // 更新访问队列
                accessQueue.remove(key);
                accessQueue.offer(key);

                return super.put(key, value);
            }
        }

        @Override
        public V get(Object key) {
            V value = super.get(key);
            if (value != null) {
                synchronized (accessQueue) {
                    accessQueue.remove(key);
                    accessQueue.offer((K) key);
                }
            }
            return value;
        }

        public static class Builder<K, V> {
            private int maxSize = 100;

            public Builder<K, V> maximumWeightedCapacity(int maxSize) {
                this.maxSize = maxSize;
                return this;
            }

            public ConcurrentLinkedHashMap<K, V> build() {
                return new ConcurrentLinkedHashMap<>(maxSize);
            }
        }
    }
}
