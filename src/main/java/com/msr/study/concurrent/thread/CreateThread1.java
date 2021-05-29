package com.msr.study.concurrent.thread;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 15:24
 */

public class CreateThread1 extends Thread {
    @Override
    public void run() {
        System.out.println("thread run by extends Thread");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new CreateThread1());
        thread.start();
    }
}


