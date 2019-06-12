package com.imooc.practice;

/**
 * 快速排序
 * @author daile.sun
 * @date 2019/4/8
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] a = {10, 8, 23, 12, 8, 45, 5, 89, 6, 3, 5, 34};
        quickSort(a, 0, a.length - 1);
        for (int i : a) {
            System.out.print(i + "  ");
        }
    }

    public static void quickSort(int[] a, int l, int r) {
        if (l < r) {
            int i, j, x;
            i = l;
            j = r;
            x = a[i];
            while (i < j) {
                while (i < j && a[j] > x) {
                    j--;
                }
                if (i < j) {
                    a[i++] = a[j];
                }
                while (i < j && a[i] < x) {
                    i++;
                }
                if (i < j) {
                    a[j--] = a[i];
                }

            }

            a[i] = x;
            quickSort(a, l, i - 1);
            quickSort(a, i + 1, r);
        }


    }

}
