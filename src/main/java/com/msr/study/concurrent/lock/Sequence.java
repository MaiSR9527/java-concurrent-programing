package com.msr.study.concurrent.lock;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/2 14:37
 */

public class Sequence {

    private int value;

    /**
     * synchronized 放在普通方法上，内置锁就是当前类的实例
     * @return
     */
    public synchronized int getNext() {
        return value ++;
    }

    public static synchronized int getPrevious() {
//		return value --;
        return 0;
    }

    public int xx () {

        // monitorenter
        synchronized (Sequence.class) {

            if(value > 0) {
                return value;
            } else {
                return -1;
            }

        }
        // monitorexit

    }

    public static void main(String[] args) {

        Sequence s = new Sequence();
//		while(true) {
//			System.out.println(s.getNext());
//		}

        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    System.out.println(Thread.currentThread().getName() + " " + s.getNext());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    System.out.println(Thread.currentThread().getName() + " " + s.getNext());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    System.out.println(Thread.currentThread().getName() + " " + s.getNext());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}

