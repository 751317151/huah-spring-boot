package com.huah.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReliablePrintABC {
    // 共享状态：0 -> A, 1 -> B, 2 -> C
    private static volatile int state = 0;
    private static final ReentrantLock lock = new ReentrantLock();
    
    // 三个条件变量，分别对应 A、B、C 的等待队列
    private static final Condition conditionA = lock.newCondition();
    private static final Condition conditionB = lock.newCondition();
    private static final Condition conditionC = lock.newCondition();

    private static final int ROUNDS = 10; // 打印轮数

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> printLetter("A", 0, conditionA, conditionB), "Thread-A");
        Thread threadB = new Thread(() -> printLetter("B", 1, conditionB, conditionC), "Thread-B");
        Thread threadC = new Thread(() -> printLetter("C", 2, conditionC, conditionA), "Thread-C");

        threadA.start();
        threadB.start();
        threadC.start();
    }

    /**
     * 打印指定字母的线程逻辑
     *
     * @param letter      要打印的字母
     * @param targetState 该字母对应的 state 值
     * @param self        自己的 Condition（用于等待）
     * @param next        下一个线程的 Condition（用于唤醒）
     */
    private static void printLetter(String letter, int targetState,
                                   Condition self, Condition next) {
        for (int i = 0; i < ROUNDS; i++) {
            lock.lock();
            try {
                // 关键：用 while 而不是 if！防止虚假唤醒
                while (state != targetState) {
                    self.await(); // 不该我执行，挂起等待
                }
                System.out.print(letter);
                state = (state + 1) % 3; // 更新状态
                next.signal();           // 精准唤醒下一个线程
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
            } finally {
                lock.unlock(); // 必须在 finally 中释放锁
            }
        }
    }
}