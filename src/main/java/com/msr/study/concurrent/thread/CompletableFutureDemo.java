package com.msr.study.concurrent.thread;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:06
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            System.out.println(finished());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(Thread.currentThread().getName()+":");

        }, "thread-1").start();

        new Thread(() -> {
            System.out.println(finished());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(Thread.currentThread().getName()+":");
        }, "thread-2").start();

    }

    public static boolean finished() {
        return CompletableFuture.supplyAsync(() -> 1 + 1, Executors.newFixedThreadPool(5))
                .thenApply((r) -> r + 1)
                .complete(3);
    }
}
