package com.msr.study.concurrent.ssy;

import java.util.concurrent.locks.LockSupport;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-19 23:52:04
 */
public class LockSupportPrint {

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(1);
                LockSupport.park();
            }
        }, "thread-a");

        thread1.start();

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(0);
                LockSupport.unpark(thread1);
            }
        }, "thread-b");

        thread2.start();

        while (thread1.isAlive()|| thread2.isAlive()){

        }
    }
}
