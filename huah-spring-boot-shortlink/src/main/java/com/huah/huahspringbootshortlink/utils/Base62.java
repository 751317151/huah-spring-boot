package com.huah.huahspringbootshortlink.utils;

public class Base62 {
    private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int BASE = 62;

    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(CHARS[(int)(num % BASE)]);
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}