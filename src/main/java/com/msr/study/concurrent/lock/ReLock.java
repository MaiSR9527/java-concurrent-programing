package com.msr.study.concurrent.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/7/7 14:18
 */

public class ReLock {

    private volatile static int i = 0;
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            System.out.println(++i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
