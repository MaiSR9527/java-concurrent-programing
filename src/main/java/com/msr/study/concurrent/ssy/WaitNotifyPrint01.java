package com.msr.study.concurrent.ssy;

/**
 * 交替打印0，1
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-05-29 12:32:33
 */
public class WaitNotifyPrint01 {

    public static void main(String[] args) {
        Counter counter = new Counter();
        EvenOdd100 evenOdd100 = new EvenOdd100();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                counter.incr();
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                counter.desc();
            }
        }).start();

//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                counter.incr();
//            }
//        }).start();
//
//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                counter.desc();
//            }
//        }).start();

//        new Thread(evenOdd100::print).start();
//
//        new Thread(evenOdd100::print).start();


    }
}

class Counter {
    private int count;
    private static final Object obj = new Object();

    public void incr() {
        synchronized (obj) {
            while (count != 0) {
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            System.out.println(count);
            obj.notify();
        }

    }

    public void desc() {
        synchronized (obj) {
            while (count == 0) {
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count--;
            System.out.println(count);
            obj.notify();
        }
    }
}

class EvenOdd100 {
    private int count = 0;
    private static final Object obj = new Object();

    public void print() {
        while (count <= 100) {
            synchronized (obj) {
                // 拿到锁就打印
                System.out.println(Thread.currentThread().getName() + ": " + count++);
                // 唤醒其他线程
                obj.notifyAll();
                try {
                    if (count <= 100) {
                        // 如果任务还没有结束，则让出当前的锁并休眠
                        obj.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
