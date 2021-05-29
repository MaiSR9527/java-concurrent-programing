package com.msr.study.concurrent.disruptor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/7/4 17:59
 */

public class Application implements Runnable {
    private static volatile AtomicInteger i = new AtomicInteger(0);
    public static void main(String[] args) {
        Application application = new Application();
        Thread thread1 = new Thread(application);
        Thread thread2 = new Thread(application);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(i);
    }


    @Override
    public void run() {
        for (int j = 0; j < 10; j++) {
            increment();
        }
    }

    public void increment(){
        i.incrementAndGet();
    }
}