package com.oneliang.test.bit;

import com.oneliang.util.common.MathUtil;

public class Test {

    public static void main(String[] args) {
        int value = 0;
        int a = 0x1;
        int b = 0x2;
        int c = 0x8;
        value |= a;
        value |= b;
        value |= c;
        System.out.println(Integer.toBinaryString(value));
        if ((value & a) == a) {
            System.out.println("has a");
        }
        if ((value & b) == b) {
            System.out.println("has b");
        }
        if ((value & c) == c) {
            System.out.println("has c");
        }
        System.out.println(~c);// ~c==Integer.MIN_VALUE+Integer.MAX_VALUE-c
        System.out.println(Integer.MIN_VALUE + Integer.MAX_VALUE - c);
        System.out.println(Integer.toBinaryString(~b & value));// delete b from
                                                               // value
    }
}
