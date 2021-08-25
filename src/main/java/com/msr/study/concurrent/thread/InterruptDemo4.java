package com.msr.study.concurrent.thread;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/6/1 17:09
 */
public class InterruptDemo4 {
    public static void main(String[] args) throws InterruptedException {
        // 静态方法interrupted() 会清除中断标志位
        System.out.println(Thread.currentThread().getName() + "---" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "---" + Thread.interrupted());
        System.out.println("111111");
        Thread.currentThread().interrupt();
        System.out.println("222222");
        System.out.println(Thread.currentThread().getName() + "---" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "---" + Thread.interrupted());
    }
}
