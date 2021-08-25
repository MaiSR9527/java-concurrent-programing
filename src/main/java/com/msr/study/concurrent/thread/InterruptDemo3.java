package com.msr.study.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class InterruptDemo3 {
    public static void main(String[] args) {
        // 发生中断异常清楚了中断标志位，Thread.currentThread().isInterrupted()为fasle。线程并没有被中止。
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()){
                    System.out.println("线程中断");
                    break;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("hello");
            }
        }, "thread-1");

        thread.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}
