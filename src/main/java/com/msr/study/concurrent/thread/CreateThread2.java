package com.msr.study.concurrent.thread;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 15:27
 */

public class CreateThread2 implements Runnable {

    @Override
    public void run() {
        System.out.println("Thread run by implements Runnable");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new CreateThread2());
        thread.start();
    }
}
