package com.msr.study.concurrent.thread;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */

public class CreateThread6 {

    public static void main(String[] args) {

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName() + " timertask is run");
//            }
//        }, 0, 1000);
        CreateThread6.test();
    }
    private static long start;
    public static void test() {
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                //timer1开始
                System.out.println(Thread.currentThread().getName()+" task1 start: " + (System.currentTimeMillis() - start));
                try {
                    //线程休眠3秒
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+" task2 start: "
                        + (System.currentTimeMillis() - start));
            }
        };
        Timer timer = new Timer();
        start = System.currentTimeMillis();
        timer.schedule(task1, 1000);
        timer.schedule(task2, 3000);
    }

}
