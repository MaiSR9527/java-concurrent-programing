package com.msr.study.concurrent.pool;

import java.util.concurrent.*;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-15 17:49:13
 */
public class ThreadPoolExecutorDemo1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );

        threadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName());
        });

        Future<String> future = threadPoolExecutor.submit(() -> Thread.currentThread().getName());
        System.out.println(future.get());
    }
}
