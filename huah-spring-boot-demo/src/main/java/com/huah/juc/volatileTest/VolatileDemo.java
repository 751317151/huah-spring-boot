package com.huah.juc.volatileTest;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @Author huah
 * @Date 2024-10-11 16:32
 */
public class VolatileDemo {
    static volatile int cnt = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(VolatileDemo::addCnt1);

        Thread thread2 = new Thread(() -> addCnt2());
        Runnable addCnt2 = VolatileDemo::addCnt2;
        Supplier<String> addCnt3 = VolatileDemo::addCnt3;
        Callable<String> addCnt31 = VolatileDemo::addCnt3;
        thread2.start();
        thread1.start();
        thread1.join();
        thread2.join();

        System.out.println("cnt : " + cnt);
    }

    public static void addCnt1() {
        for (int i = 0; i < 10000; i++) {
            cnt++;
        }
    }

    public static void addCnt2() {
        for (int i = 0; i < 10000; i++) {
            cnt++;
        }
    }

    public static String addCnt3() {
        for (int i = 0; i < 10000; i++) {
            cnt++;
        }
        return "";
    }
}
