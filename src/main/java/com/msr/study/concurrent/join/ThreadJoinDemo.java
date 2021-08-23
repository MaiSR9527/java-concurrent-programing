package com.msr.study.concurrent.join;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class ThreadJoinDemo {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> System.out.println(Thread.currentThread().getName()), "thread-1");
        Thread thread2 = new Thread(() -> System.out.println(Thread.currentThread().getName()), "thread-2");
        Thread thread3 = new Thread(() -> System.out.println(Thread.currentThread().getName()), "thread-3");
        Thread thread4 = new Thread(() -> System.out.println(Thread.currentThread().getName()), "thread-4");

        try {
            thread1.join();
            thread1.start();

            thread2.join();
            thread2.start();

            thread3.join();
            thread3.start();

            thread4.join();
            thread4.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
