package com.huah.juc;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author huah 2024/03/15 13:23
 */
public class BasicLockDemo {
    private static Object objectLock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(ClassLayout.parseInstance(objectLock).toPrintable());
            }
        }, "t1").start();
        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(ClassLayout.parseInstance(objectLock).toPrintable());
            }
        }, "t1").start();
    }
}
