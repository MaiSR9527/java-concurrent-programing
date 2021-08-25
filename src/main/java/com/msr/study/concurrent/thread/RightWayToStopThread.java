package com.msr.study.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-06-02 23:58:43
 */
public class RightWayToStopThread {

    public static void main(String[] args) {
        Thread thread = new Thread(new Demo());
        thread.start();
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}

class Demo implements Runnable{

    @Override
    public void run() {
        System.out.println("go");
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted，程序运行结束");
                break;
            }
            reInterrupt();
        }
    }

    private void reInterrupt() {
        // 自己处理中断
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // 如果不调用，中断信息就会被独吞，上层调用者就会无法感知
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
