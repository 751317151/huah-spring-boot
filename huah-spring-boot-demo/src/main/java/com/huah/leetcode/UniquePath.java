package com.huah.leetcode;

/**
 * @author huah@sunwayworld.com 2025/02/26 14:30
 */
public class UniquePath {

    public static void main(String[] args) {
        System.out.println(uniquePaths(3,7));
        System.out.println(uniquePaths(1,1));
        System.out.println(uniquePaths(1,2));
        System.out.println(uniquePaths(2,2));
    }

    /**
     * 机器人从 m x n 网格的左上角出发，每次只能向右或向下移动，求到达右下角的路径总数。
     *
     * 输入示例：
     *  m = 3, n = 7
     * 输出：28
     * @param m
     * @param n
     * @return
     */
    public static int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
        return dp[m-1][n-1];
    }
}
