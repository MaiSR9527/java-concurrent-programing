package com.msr.study.concurrent.deadlock;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 18:37
 */

public class ThreadDeadLock {

    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new HoldThread(lockA, lockB), "threadA").start();
        new Thread(new HoldThread(lockB, lockA), "threadB").start();
    }

}

class HoldThread implements Runnable {

    private final String source1;
    private final String source2;

    public HoldThread(String source1, String source2) {
        this.source1 = source1;
        this.source2 = source2;
    }

    @Override
    public void run() {
        synchronized (source1) {
            System.out.println(Thread.currentThread().getName() + "\t 持有锁" + source1 + "尝试获得" + source2);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (source2) {
                System.out.println(Thread.currentThread().getName() + "\t 持有锁" + source2 + "尝试获得" + source1);
            }
        }
    }
}
