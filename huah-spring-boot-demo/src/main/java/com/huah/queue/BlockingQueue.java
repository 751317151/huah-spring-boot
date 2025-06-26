package com.huah.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int maxSize; // 队列最大容量
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();  // 队列不满条件
    private final Condition notEmpty = lock.newCondition(); // 队列不空条件

    private volatile boolean running = true; // 队列运行状态标志

    public BlockingQueue(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("队列大小必须大于0");
        }
        this.maxSize = maxSize;
    }

    // 生产者：阻塞添加元素
    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == maxSize) {
                notFull.await(); // 队列满时等待
            }
            queue.add(item);
            System.out.println("生产: " + item);
            notEmpty.signal(); // 只唤醒消费者线程
        } finally {
            lock.unlock();
        }
    }

    // 非阻塞添加元素
    public boolean offer(T item) {
        lock.lock();
        try {
            if (queue.size() < maxSize) {
                queue.add(item);
                System.out.println("生产(非阻塞): " + item);
                notEmpty.signal();
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    // 限时阻塞添加元素
    public boolean offer(T item, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            while (queue.size() == maxSize) {
                if (nanos <= 0L) {
                    return false; // 超时未添加成功
                }
                nanos = notFull.awaitNanos(nanos);
            }
            queue.add(item);
            System.out.println("生产(限时): " + item);
            notEmpty.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    // 消费者：阻塞获取元素
    public T take() throws InterruptedException {
        lock.lock();
        try {
            // 修改等待条件：在队列为空时，如果队列还在运行，则等待；如果已经关闭，则返回null
            while (queue.isEmpty()) {
                if (!running) {
                    return null; // 或者抛出一个ClosedQueueException
                }
                System.out.println("队列running状态: " + running);
                System.out.println("线程--" + Thread.currentThread().getName() + " 进入等待");
                notEmpty.await(); // 队列空时等待
            }
            T item = queue.poll();
            System.out.println("消费: " + item);
            notFull.signal(); // 只唤醒生产者线程
            return item;
        } finally {
            lock.unlock();
        }
    }

    // 非阻塞获取元素
    public T poll() {
        lock.lock();
        try {
            if (!queue.isEmpty()) {
                T item = queue.poll();
                System.out.println("消费(非阻塞): " + item);
                notFull.signal();
                return item;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    // 限时阻塞获取元素
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            while (queue.isEmpty()) {
                if (nanos <= 0L) {
                    return null; // 超时未获取元素
                }
                nanos = notEmpty.awaitNanos(nanos);
            }
            T item = queue.poll();
            System.out.println("消费(限时): " + item);
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }

    // 关闭队列，唤醒所有等待线程
    public void shutdown() {
        lock.lock();
        try {
            running = false;
            System.out.println("shutdown 唤醒 ");
            notFull.signalAll();
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // 获取队列当前大小
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new BlockingQueue<>(5);

        // 生产者线程
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    // 使用限时添加，避免死锁
                    if (!queue.offer(i, 1, TimeUnit.SECONDS)) {
                        System.out.println("生产者超时: " + i);
                    }
                    Thread.sleep(300); // 生产间隔
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Producer");

        // 第二个生产者线程
        Thread secondProducer = new Thread(() -> {
            for (int i = 10; i < 20; i++) {
                try {
                    if (queue.offer(i)) { // 非阻塞添加
                        Thread.sleep(150); // 更快的生产
                    } else {
                        System.out.println("生产者跳过: " + i);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "SecondProducer");

        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 15; i++) {
                    // 使用限时获取
                    Integer item = queue.poll(1, TimeUnit.SECONDS);
                    if (item != null) {
                        // 处理元素
                        System.out.println("消费者1处理元素: " + item);
                    } else {
                        System.out.println("消费者1等待超时");
                    }
                    Thread.sleep(500); // 消费间隔
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        // 第二个消费者线程
        Thread secondConsumer = new Thread(() -> {
            try {
                for (int i = 0; i < 15; i++) {
                    Integer item = queue.take(); // 阻塞获取
                    if (item != null) {
                        // 处理元素
                        System.out.println("消费者2处理元素: " + item);
                    } else {
                        System.out.println("消费者2等待超时");
                    }
                    Thread.sleep(400); // 不同的消费速度
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "SecondConsumer");

        // 监视线程 - 打印队列状态
        Thread monitor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    System.out.println("队列大小: " + queue.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Monitor");

        // 启动所有线程
        producer.start();
        secondProducer.start();
        consumer.start();
        secondConsumer.start();
        monitor.start();

        // 等待所有线程完成
        try {
            producer.join();
            secondProducer.join();
            // 等待消费者完成
            Thread.sleep(2000); // 给消费者额外时间
            queue.shutdown(); // 关闭队列
            consumer.join(1000);
            secondConsumer.join(1000);
            monitor.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("所有任务完成");
    }
}