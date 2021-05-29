package com.msr.study.concurrent.thread;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 15:30
 */

public class CreateThread3 {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread run by inner class");
            }
        }).start();
        //lambda写法
        new Thread(()-> System.out.println("Thread run by inner class")).start();
    }
}
