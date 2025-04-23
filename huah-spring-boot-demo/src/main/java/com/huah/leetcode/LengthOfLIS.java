package com.huah.leetcode;

import java.util.Arrays;

/**
 * @author huah@sunwayworld.com 2025/02/26 15:10
 */
public class LengthOfLIS {

    public static void main(String[] args) {
        int[] nums = new int[]{0,1,0,3,2,3};
        System.out.println(lengthOfLIS(nums));
    }

    /**
     * 给定整数数组 nums，找到最长严格递增子序列的长度。
     *
     * 输入示例：
     *
     * 输入：nums = [10,9,2,5,3,7,101,18]  
     * 输出：4  
     * 解释：最长递增子序列是 [2,3,7,101]，长度为4。
     * @return
     */
    public static int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int maxValue = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxValue = Math.max(maxValue, dp[i]);
        }
        return maxValue;
    }
}
