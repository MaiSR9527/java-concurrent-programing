package com.msr.study.concurrent.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:06
 */

public class CreateThread5 {
    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            threadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
        }

        threadPool.shutdown();
    }
}
