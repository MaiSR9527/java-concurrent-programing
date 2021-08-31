package com.msr.study.concurrent.monitorlock;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-29 13:35:32
 */
public class UpgradeLockDemo {
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(object).toPrintable());
        System.out.println("==============================");
        Thread thread = new Thread(() -> {
//            for (int i = 0; i < 5; i++) {
            biasedLock();
//            }
        });
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();
        Thread thread1 = new Thread(() -> {
//            for (int i = 0; i < 5; i++) {
            biasedLock();
//            }
        });
        thread1.start();
    }

    static Object object = new Object();

    private static void biasedLock() {
        synchronized (object) {
            System.out.println(Thread.currentThread().getName());
            System.out.println(ClassLayout.parseInstance(object).toPrintable());
        }
    }

    private static void noLock() {
        Object o = new Object();
        System.out.println(o.hashCode());
        System.out.println(Integer.toHexString(o.hashCode()));
        System.out.println(Integer.toBinaryString(o.hashCode()));

        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}

class TrainTicket {
    private int number = 50;

    Object objectLock = new Object();

    public void sale() {
        synchronized (objectLock) {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "\t" + "---卖出第： " + (number--));
            }
        }
    }
}