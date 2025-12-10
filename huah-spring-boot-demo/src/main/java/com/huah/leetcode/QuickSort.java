package com.huah.leetcode;

public class QuickSort {

    public static void quickSort(int[] arr, int left, int right) {
        if (left >= right) return;

        int pivotIndex = partition(arr, left, right);
        quickSort(arr, left, pivotIndex - 1);   // 递归排序左半部分
        quickSort(arr, pivotIndex + 1, right);  // 递归排序右半部分
    }

    private static int partition(int[] arr, int left, int right) {
        int pivot = arr[left]; // 选择最后一个元素作为基准
        int left1 = left + 1;

        while (left1<= right) {
            if (arr[left1] < pivot) {
                left1++;
            }else if (arr[right] > pivot) {
                right--;
            } else {
                swap(arr, left1, right);
                left1++;
                right--;
            }
        }
        swap(arr, left, right);
        return right;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {3, 6, 8, 10, 1, 2, 1};
        quickSort(arr, 0, arr.length - 1);
        for (int num : arr) {
            System.out.print(num + " ");
        }
        // 输出: 1 1 2 3 6 8 10
    }
}