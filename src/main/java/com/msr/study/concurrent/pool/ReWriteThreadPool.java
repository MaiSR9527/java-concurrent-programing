package com.msr.study.concurrent.pool;


import java.util.concurrent.*;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class ReWriteThreadPool {

    public static void main(String[] args) {
        MyThreadPoolExecutor executor = new MyThreadPoolExecutor(
                5,
                10,
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        executor.execute(()->{
            System.out.println("run something");
        });
        try {
            TimeUnit.SECONDS.sleep(2);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyThreadPoolExecutor extends ThreadPoolExecutor {

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        /**
         * 线程开始执行时调用的钩子
         *
         * @param t 要执行的线程
         * @param r 要执行的任务
         */
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("============before============");
            System.out.println(Thread.currentThread().getName());
            System.out.println(t.getThreadGroup());
        }

        /**
         * 线程执行完成之后调用的钩子
         *
         * @param r 执行完成的任务
         * @param t 是否发生异常，不为null则发生异常，可以做相应的处理
         */
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            System.out.println("============after============");
            System.out.println(Thread.currentThread().getName());
            if (t != null) {
                System.out.println("having exception");
            }
        }

        /**
         * 线程池中止时调用的钩子，可以在此监控线程池是否挂了
         */
        @Override
        protected void terminated() {
            System.out.println("============terminated============");
            System.out.println(Thread.currentThread().getName());
            System.out.println("what terminated!");
        }
    }
}
