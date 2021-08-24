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

    static class MyThreadPoolExecutor extends ThreadPoolExecutor{

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("============before============");
            System.out.println(Thread.currentThread().getName());
            System.out.println(t.getThreadGroup());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            System.out.println("============after============");
            System.out.println(Thread.currentThread().getName());
            if (t!=null){
                System.out.println("having exception");
            }
        }

        @Override
        protected void terminated() {
            System.out.println("============terminated============");
            System.out.println(Thread.currentThread().getName());
            System.out.println("what terminated!");
        }
    }
}
