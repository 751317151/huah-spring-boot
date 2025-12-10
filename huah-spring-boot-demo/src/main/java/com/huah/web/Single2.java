package com.huah.web;

/**
 * @Author BlackStar
 * @Date 2025-11-16 20:03:36
 */
public class Single2 {
    private Single2(){}

    private static class SingleHolder{
        private static final Single2 single = new Single2();
    }

    public static Single2 getInstance(){
        return SingleHolder.single;
    }
}
