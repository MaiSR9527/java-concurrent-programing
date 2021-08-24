package com.msr.study.concurrent.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class GracefulClosePool {
    public static void shutdownThreadPoolGracefully(ExecutorService threadPool) {
        if (threadPool == null || threadPool.isTerminated()) {
            return;
        } else {
            // 关闭线程池，拒绝接收新任务
            threadPool.shutdown();
            try {
                // 等待60秒，等待线程池中的任务完成执行
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    // 调用 shutdownNow() 方法取消正在执行的任务
                    threadPool.shutdownNow();
                    if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.err.println("线程池任务未正常执行结束");
                    }
                }
            } catch (InterruptedException ie) {
                // 捕获异常，重新调用 shutdownNow() 方法
                threadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
            // 仍然没有关闭，循环关闭1000次，每次等待10毫秒
            if (!threadPool.isTerminated()) {
                try {
                    for (int i = 0; i < 1000; i++) {
                        if (threadPool.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                            break;
                        }
                        threadPool.shutdownNow();
                    }
                } catch (Throwable e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }

    /**
     * 懒汉式单例创建线程池：用于执行定时、顺序任务
     */
    static class SeqOrScheduledTargetThreadPoolLazyHolder {
        //线程池：用于定时任务、顺序排队执行任务
        static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory());

        static {
            // 重点：添加JVM关闭时候的钩子
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                //优雅地关闭线程池
                shutdownThreadPoolGracefully(EXECUTOR);
            }));
        }
    }
}
