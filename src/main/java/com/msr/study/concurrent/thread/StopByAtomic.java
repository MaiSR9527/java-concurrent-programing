package com.msr.study.concurrent.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class StopByAtomic {

    private final static AtomicBoolean flag = new AtomicBoolean(true);

    public static void main(String[] args) {
        new Thread(() -> {
            while (flag.get()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("-----I am die");
            }
        }, "thread-1").start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag.set(false);
    }
}
