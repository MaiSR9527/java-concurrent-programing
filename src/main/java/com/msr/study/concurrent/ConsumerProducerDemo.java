package com.msr.study.concurrent;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MaiShuRen
 * @version v1.0
 * @date 2020/7/7 16:34
 */
public class ConsumerProducerDemo {

    private static final int MAX_AMOUNT = 10;
    private BlockingDeque<Integer> queue = new LinkedBlockingDeque<>();

    private AtomicInteger amount = new AtomicInteger(0);

    public static void main(String[] args) {

    }
}
