package com.msr.study.concurrent.thread;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 16:45
 */

public class CreateThread4 {
    public static void main(String[] args) {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + " start");
            TimeUnit.SECONDS.sleep(3);
            System.out.println(Thread.currentThread().getName() + " end");
            return 1 + 1;
        });

        Thread thread = new Thread(task);
        thread.start();
        try {
            System.out.println(Thread.currentThread().getName()+" "+task.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" 结束");
    }
}
