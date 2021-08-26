package com.msr.study.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/7/7 16:34
 */

public class CountDownLatchDemo {

    private static final int MAX_THREAD = 10;

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(MAX_THREAD);

        for (int i = 0; i < MAX_THREAD; i++) {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
                countDownLatch.countDown();
            }, "thread" + i).start();

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("over");
    }

}
