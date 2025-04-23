package com.huah.leetcode;

import java.util.Arrays;

/**
 * @author huah@sunwayworld.com 2025/02/27 11:33
 */
public class sumTest {
    public static void main(String[] args) {
        System.out.println(sum(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }

    public static int sum(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, nums[0]);
        int maxValue = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(nums[i],dp[i-1] + nums[i]);
            maxValue = Math.max(dp[i], maxValue);
        }
        return maxValue;
    }
}
