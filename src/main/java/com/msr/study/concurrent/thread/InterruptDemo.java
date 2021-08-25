package com.msr.study.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class InterruptDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("-----t1 线程被中断了，break，程序结束");
                    break;
                }
                System.out.println("-----hello");
            }
        }, "t1");
        t1.start();

        System.out.println("**************" + t1.isInterrupted());
        //暂停5毫秒
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.interrupt();
        System.out.println("**************" + t1.isInterrupted());
    }
}
