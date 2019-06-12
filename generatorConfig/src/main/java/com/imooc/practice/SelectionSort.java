package com.imooc.practice;

/**
 * 选择排序
 * @author daile.sun
 * @date 2019/4/9
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] a = {10, 8, 23, 12, 8, 45, 5, 89, 6, 3, 5, 34};
        selectionSort(a);
        for (int i : a) {
            System.out.print(i + "  ");
        }
    }

    public static void selectionSort(int[] a){
        for(int i=0;i<a.length;i++){
            for(int j=i+1;j<a.length;j++){
                if(a[i]>a[j]){
                    int temp;
                    temp=a[i];
                    a[i]=a[j];
                    a[j]=temp;
                }
            }
        }
    }
}
