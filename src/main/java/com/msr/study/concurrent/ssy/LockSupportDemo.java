package com.msr.study.concurrent.ssy;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-19 23:42:27
 */
public class LockSupportDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("coming in");
            LockSupport.park();
            System.out.println("one park");
//            LockSupport.park();
//            System.out.println("two park");
        });

        thread.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 unpark");
            LockSupport.unpark(thread);
        });

        thread2.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread3 = new Thread(() -> {
            System.out.println("thread3 unpark");
            LockSupport.unpark(thread);
        });

        thread3.start();

    }
}
