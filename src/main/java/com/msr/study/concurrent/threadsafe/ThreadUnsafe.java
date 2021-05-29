package com.msr.study.concurrent.threadsafe;

import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/2 10:49
 */

public class ThreadUnsafe {


    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(ticket);
            thread.start();
        }
    }
}

class Ticket implements Runnable {
    private static int ticketNum = 20;

    @Override
    public void run() {
        while (true) {
            if (ticketNum > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " sale a ticket,current:" + saleTicket());
            }else {
                System.out.println("票已经卖完了");
                break;
            }
        }
    }

    public int saleTicket(){
        return ticketNum--;
    }
}
