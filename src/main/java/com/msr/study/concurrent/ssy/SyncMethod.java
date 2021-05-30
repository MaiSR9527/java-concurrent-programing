package com.msr.study.concurrent.ssy;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-05-30 16:51:56
 */
public class SyncMethod {

    public static void main(String[] args) {
        new Thread(()->{
            SyncDemo syncDemo = new SyncDemo();
            syncDemo.hello();
        }).start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            SyncDemo syncDemo = new SyncDemo();
            syncDemo.world();
        }).start();
    }
}

class SyncDemo {
    public synchronized void hello() {
        System.out.println("hello");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void world() {
        System.out.println("world");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
