package com.imooc.practice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author daile.sun
 * @date 2018/11/12
 */
public class Lambda {
    public static void main(String[] args) {
        List<String> name= Arrays.asList("b","a","d","c");
//        Collections.sort(name,(a, b)-> a.compareTo(b));

        String[] names={"b","a","d","c"};
        Arrays.sort((String [])name.toArray(), (s1,s2) -> (s1.compareTo(s2)));
        name.forEach((n)-> System.out.println(n));
//        name.forEach(System.out::println);
        Arrays.sort(names);
        for (int i = 0; i <names.length ; i++) {
            System.out.println(names[i]);
        }

    }
}
