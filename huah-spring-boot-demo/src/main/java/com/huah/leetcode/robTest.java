package com.huah.leetcode;

/**
 * @author huah@sunwayworld.com 2025/02/27 11:10
 */
public class robTest {

    public static void main(String[] args) {
        int[] nums = new int[]{0,1,0,3,2,3};
        System.out.println(rob(nums));
        System.out.println(rob(new int[]{2,7,9,5,1,3}));
    }

    public static int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;
        int[][] dp = new int[n][2];
        dp[0][0] = 0;
        dp[0][1] = nums[0];
        for (int i = 1; i < n; i++) {
            dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1]);
            dp[i][1] = dp[i-1][0] + nums[i];
        }
        return Math.max(dp[n-1][0], dp[n-1][1]);
    }
}
