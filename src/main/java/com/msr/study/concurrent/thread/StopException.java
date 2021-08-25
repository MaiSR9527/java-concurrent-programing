package com.msr.study.concurrent.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class StopException {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                // wait sleep join 期间被中断会触犯中断异常
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        thread.interrupt();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
