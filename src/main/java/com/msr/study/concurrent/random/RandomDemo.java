package com.msr.study.concurrent.random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-15 17:49:13
 */
public class RandomDemo {
    public static void main(String[] args) {
        localRandomTest();
        randomTest();
    }

    private static void localRandomTest() {
        long start = System.currentTimeMillis();
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        for (int i = 0; i < 100000; i++) {
            // 0-100之间不包含100
            System.out.println(localRandom.nextInt(100));
        }
        System.out.print(Thread.currentThread().getName() + "耗时ms：");
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void randomTest() {
        long start = System.currentTimeMillis();
        // 1 创建一个默认种子的随机数生成器
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            // 0-100之间不包含100
            System.out.println(random.nextInt(100));
        }
        System.out.print(Thread.currentThread().getName() + "耗时ms：");
        System.out.println(System.currentTimeMillis() - start);
    }
}
