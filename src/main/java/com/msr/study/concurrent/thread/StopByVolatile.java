package com.msr.study.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class StopByVolatile {

    private static volatile boolean isStop = false;

    public static void main(String[] args) {

        // 一直死循环，大部分时间会空转，浪费CPU时间
        new Thread(() -> {
            while (true) {
                if (isStop) {
                    System.out.println(Thread.currentThread().getName() + "线程------isStop = true,自己退出了");
                    break;
                }
                System.out.println("-------hello interrupt");
            }
        }, "thread-1").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isStop = true;
    }
}
