package com.huah.leetcode;

/**
 * @Author BlackStar
 * @Date 2025-11-14 22:57:24
 */
public class MaxProfile {

    public static void main(String[] args) {
        int[] prices = new int[]{7,1,5,3,6,4};
        System.out.println(maxProfit(prices));
        String s1 = "hello";
        String s2 = "he" + new String("llo");
        System.out.println(s1 == s2);
    }

    public static int maxProfit(int[] prices) {
        int profile = 0;
        int min = prices[0];
        for(int i = 0; i < prices.length ;i++) {
            int price = prices[i];
            if (price < min) {
                min = price;
            }
            if (profile < price - min) {
                profile = price - min;
            }
        }
        return profile;
    }
}
