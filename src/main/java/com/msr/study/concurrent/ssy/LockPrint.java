package com.msr.study.concurrent.ssy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-20 00:00:57
 */
public class LockPrint {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    System.out.println(1);
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }, "thread-a");

        thread1.start();

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    System.out.println(0);
                    condition.signal();
                } finally {
                    lock.unlock();
                }
            }
        }, "thread-b");

        thread2.start();

        while (thread1.isAlive() || thread2.isAlive()) {

        }
    }

    public static void print1() {

    }

    public static void print0() {

    }
}
