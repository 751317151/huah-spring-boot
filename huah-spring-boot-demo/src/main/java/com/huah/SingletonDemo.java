package com.huah;

/**
 * @Author BlackStar
 * @Date 2025-11-16 19:09:49
 */
public class SingletonDemo {
    private SingletonDemo(){}
    private volatile static  SingletonDemo instance;

    public static SingletonDemo getInstance() {
        if (instance == null) {
            synchronized (SingletonDemo.class) {
                if (instance == null) {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        SingletonDemo instance1 = SingletonDemo.getInstance();
        System.out.println(instance1);
    }
}
