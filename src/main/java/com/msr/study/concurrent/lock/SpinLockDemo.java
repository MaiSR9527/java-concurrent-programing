package com.msr.study.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-23 23:52:54
 */
public class SpinLockDemo {

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock() {
        Thread thread = Thread.currentThread();
        while (!atomicReference.weakCompareAndSet(null, thread)) {
            System.out.println(thread.getName() + " try to lock!But fail!");
        }
        System.out.println(thread.getName() + " lock success!");
    }

    public void unLock() {
        Thread thread = Thread.currentThread();
        while (!atomicReference.weakCompareAndSet(thread, null)) {
            System.out.println(thread.getName() + " try to unLock!But fail!");
        }
        System.out.println(thread.getName() + " unLock success!");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(() -> {
            spinLockDemo.lock();
            try {
                TimeUnit.MICROSECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                spinLockDemo.unLock();
            }
        }, "thread-1").start();

        new Thread(() -> {
            spinLockDemo.lock();
            try {
                TimeUnit.MICROSECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                spinLockDemo.unLock();
            }
        }, "thread-2").start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
