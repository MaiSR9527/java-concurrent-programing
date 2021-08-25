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
public class CPUPoolDemo {

    /**
     * CPU核数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final int QUEUE_SIZE = 30;

    private static class CpuIntenseTargetThreadPoolLazyHolder {
        //线程池：用于CPU密集型任务
        private static final ThreadPoolExecutor EXECUTOR =
                new ThreadPoolExecutor(
                        MAXIMUM_POOL_SIZE,
                        MAXIMUM_POOL_SIZE,
                        KEEP_ALIVE_SECONDS,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(QUEUE_SIZE),
                        Executors.defaultThreadFactory());

        static {
            EXECUTOR.allowCoreThreadTimeOut(true);
            //JVM关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        //优雅地关闭线程池
                        shutdownThreadPoolGracefully(EXECUTOR);
                    }));
        }
    }
}
