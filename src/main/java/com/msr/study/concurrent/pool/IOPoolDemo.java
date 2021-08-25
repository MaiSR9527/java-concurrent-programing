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
public class IOPoolDemo {

    /**
     * CPU核数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * IO处理线程数
     */
    private static final int IO_MAX = Math.max(2, CPU_COUNT * 2);

    /**
     * 任务队列大小
     */
    private static final int QUEUE_SIZE = 30;

    private static final int KEEP_ALIVE_SECONDS = 30;

    private static class IoIntenseTargetThreadPoolLazyHolder {
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                IO_MAX,
                IO_MAX,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_SIZE),
                Executors.defaultThreadFactory()
        );

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
