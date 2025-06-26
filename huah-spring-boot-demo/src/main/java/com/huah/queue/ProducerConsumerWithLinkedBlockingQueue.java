package com.huah.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProducerConsumerWithLinkedBlockingQueue {

    public static void main(String[] args) throws InterruptedException {
        // 创建一个最大容量为10的有界阻塞队列
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);
        
        // 创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // 启动两个生产者
        executor.submit(new Producer(queue, 1, 20));
        executor.submit(new Producer(queue, 101, 120));
        Thread.sleep(1000);
        // 启动两个消费者
        executor.submit(new Consumer(queue));
        executor.submit(new Consumer(queue));
        
        // 启动监控线程
        Thread monitorThread = new Thread(new QueueMonitor(queue));
        monitorThread.setDaemon(true); // 设置为守护线程
        monitorThread.start();
        
        // 关闭线程池（等待任务完成）
        executor.shutdown();
        
        // 等待所有任务完成
        if (!executor.awaitTermination(15, TimeUnit.SECONDS)) {
            System.err.println("任务超时未完成，强制结束");
            executor.shutdownNow();
        }
        
        System.out.println("所有任务完成，程序结束");
    }
}

// 生产者类
class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int start;
    private final int end;
    
    public Producer(BlockingQueue<Integer> queue, int start, int end) {
        this.queue = queue;
        this.start = start;
        this.end = end;
    }
    
    @Override
    public void run() {
        try {
            for (int i = start; i <= end; i++) {
                // 尝试生产，最多等待500ms
                boolean success = queue.offer(i, 500, TimeUnit.MILLISECONDS);
                
                if (success) {
                    System.out.printf("[生产者-%d] 生产: %d (队列大小: %d)%n", 
                                     Thread.currentThread().getId(), i, queue.size());
                } else {
                    System.err.printf("[生产者-%d] 生产 %d 超时 (队列已满)%n", 
                                    Thread.currentThread().getId(), i);
                }
                
                // 模拟生产时间
                Thread.sleep(100 + (long) (Math.random() * 200));
            }
            System.out.printf("[生产者-%d] 生产任务完成%n", Thread.currentThread().getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("[生产者-%d] 被中断%n", Thread.currentThread().getId());
        }
    }
}

// 消费者类
class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;
    
    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // 尝试消费，最多等待1秒
                Integer item = queue.poll(1, TimeUnit.SECONDS);
                
                if (item != null) {
                    System.out.printf("[消费者-%d] 消费: %d (队列大小: %d)%n", 
                                    Thread.currentThread().getId(), item, queue.size());
                    
                    // 模拟处理时间
                    Thread.sleep(150 + (long) (Math.random() * 300));
                } else {
                    // 如果连续5秒没有任务，结束消费者线程
                    if (queue.isEmpty() && !Thread.currentThread().isInterrupted()) {
                        System.out.printf("[消费者-%d] 队列长时间为空，退出%n", 
                                        Thread.currentThread().getId());
                        return;
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("[消费者-%d] 被中断%n", Thread.currentThread().getId());
        }
    }
}

// 队列监控类
class QueueMonitor implements Runnable {
    private final BlockingQueue<Integer> queue;
    private static final int REPORT_INTERVAL = 3000; // 报告间隔(ms)
    
    public QueueMonitor(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int size = queue.size();
                int capacity = queue.remainingCapacity() + size;
                double usage = (size * 100.0) / capacity;
                
                System.out.printf("[监控] 队列状态: 大小=%d, 容量=%d, 使用率=%.1f%%%n",
                                size, capacity, usage);
                
                // 队列使用率过高预警
                if (usage > 80) {
                    System.out.println("[监控] ⚠️ 警告: 队列即将满员!");
                }
                
                // 队列使用率过低预警
                if (usage < 20) {
                    System.out.println("[监控] ℹ️ 提示: 队列空闲，可减少消费者");
                }
                
                Thread.sleep(REPORT_INTERVAL);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[监控] 监控线程结束");
        }
    }
}