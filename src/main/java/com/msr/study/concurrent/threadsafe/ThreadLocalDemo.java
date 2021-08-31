package com.msr.study.concurrent.threadsafe;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-28 15:24:55
 */
public class ThreadLocalDemo {

    public static void main(String[] args) {
        ThreadLocal<Integer> integerThreadLocal = new ThreadLocal<>();
        System.out.println(integerThreadLocal.get());
    }
}
