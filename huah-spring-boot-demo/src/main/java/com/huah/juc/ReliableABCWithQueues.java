package com.huah.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ReliableABCWithQueues {
    // 每个线程一个专属队列
    private static final BlockingQueue<Integer> queueAB = new ArrayBlockingQueue<>(1);
    private static final BlockingQueue<Integer> queueBC = new ArrayBlockingQueue<>(1);
    private static final BlockingQueue<Integer> queueCA = new ArrayBlockingQueue<>(1);

    private static final int ROUNDS = 10;

    public static void main(String[] args) throws InterruptedException {
        Thread a = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    if (i > 0) {
                        queueCA.take(); // 等待 C 通知（除了第一轮）
                    }
                    System.out.print("A");
                    queueAB.put(1); // 通知 B
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "A");

        Thread b = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    queueAB.take(); // 等待 A 通知
                    System.out.print("B");
                    queueBC.put(1); // 通知 C
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "B");

        Thread c = new Thread(() -> {
            try {
                for (int i = 0; i < ROUNDS; i++) {
                    queueBC.take(); // 等待 B 通知
                    System.out.print("C");
                    queueCA.put(1); // 通知 A（下一轮）
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "C");

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();
    }
}