package com.msr.study.concurrent.deadlock;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-18 19:38:26
 */
public class DeadLockDemo {
    static Object lock1 = new Object();
    static Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + "-" + "持有lock1,尝试获取lock2");
                // 模拟业务执行1秒
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + "-" + "得到了lock2");
                }
            }
        }, "Thread-A").start();

        new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + "-" + "持有lock2,尝试获取lock1");
                // 模拟业务执行1秒
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + "-" + "得到了lock1");
                }
            }
        }, "Thread-B").start();
    }
}
