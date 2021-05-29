package com.msr.study.concurrent.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/2 13:27
 */

public class SynchronizedLock {

    public synchronized void send() {
        System.out.println(Thread.currentThread().getName()+":send email");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void phone() {
        System.out.println(Thread.currentThread().getName()+":phone");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SynchronizedLock lock1 = new SynchronizedLock();
        SynchronizedLock lock2 = new SynchronizedLock();
        new Thread(()->{
            lock1.phone();
        },"lock1").start();

        new Thread(()->{
            lock1.send();
        },"lock2").start();
    }
}

