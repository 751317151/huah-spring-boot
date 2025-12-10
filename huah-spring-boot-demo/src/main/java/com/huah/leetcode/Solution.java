package com.huah.leetcode;

import java.util.Arrays;

class Solution {

    public static void main(String[] args) {
        int[] nums = {2, 1, 0, 3, 12};
        Solution solution = new Solution();
        solution.moveZeroesToLast(nums);
        System.out.println(Arrays.toString(nums));
        solution.moveZeroesToFirst(nums);
        System.out.println(Arrays.toString(nums));

    }
    public void moveZeroesToLast(int[] nums) {
        int n = nums.length, left = 0, right = 0;
        while (right < n) {
            if (nums[right] != 0) {
                swap(nums, left, right);
                left++;
            }
            right++;
        }
    }

    public void moveZeroesToFirst(int[] nums) {
        int n = nums.length, left = n - 1, right = n - 1;
        while (left >= 0) {
            if (nums[left] != 0) {
                swap(nums, left, right);
                right--;
            }
            left--;
        }
    }

    public void swap(int[] nums, int left, int right) {
        int temp = nums[left];
        nums[left] = nums[right];
        nums[right] = temp;
    }
}