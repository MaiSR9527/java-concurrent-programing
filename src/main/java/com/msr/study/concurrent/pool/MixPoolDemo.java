package com.msr.study.concurrent.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.msr.study.concurrent.pool.GracefulClosePool.shutdownThreadPoolGracefully;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-15 17:49:13
 */
public class MixPoolDemo {
    /**
     * 最大线程数
     */
    private static final int MIXED_MAX = 128;
    private static final String MIXED_THREAD_AMOUNT = "mixed.thread.amount";
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final int QUEUE_SIZE = 30;

    private static class MixedTargetThreadPoolLazyHolder {
        //首先从环境变量 mixed.thread.amount 中获取预先配置的线程数
        //如果没有对 mixed.thread.amount进行配置，就使用常量 MIXED_MAX作为线程数
        private static final int max =
                (null != System.getProperty(MIXED_THREAD_AMOUNT)) ?
                        Integer.parseInt(System.getProperty(MIXED_THREAD_AMOUNT))
                        : MIXED_MAX;
        //线程池：用于混合型任务
        private static final ThreadPoolExecutor EXECUTOR =
                new ThreadPoolExecutor(
                        max,
                        max,
                        KEEP_ALIVE_SECONDS,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(QUEUE_SIZE),
                        Executors.defaultThreadFactory());

        static {
            EXECUTOR.allowCoreThreadTimeOut(true);
            //JVM关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        //优雅地关闭线程池
                        shutdownThreadPoolGracefully(EXECUTOR);
                    }));
        }
    }
}
