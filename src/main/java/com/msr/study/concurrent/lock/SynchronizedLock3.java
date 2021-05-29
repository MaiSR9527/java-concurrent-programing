package com.msr.study.concurrent.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/2 14:14
 */

public class SynchronizedLock3 {
    private final Object lock = new Object();
    public void send() {
        synchronized (lock){
            System.out.println(Thread.currentThread().getName()+":send email");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void  phone() {
        synchronized (lock){
            System.out.println(Thread.currentThread().getName()+":phone");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        SynchronizedLock2 lock1 = new SynchronizedLock2();
        SynchronizedLock2 lock2 = new SynchronizedLock2();
        new Thread(()->{
            lock1.phone();
        },"lock1").start();

        new Thread(()->{
            lock2.send();
        },"lock2").start();
    }
}
