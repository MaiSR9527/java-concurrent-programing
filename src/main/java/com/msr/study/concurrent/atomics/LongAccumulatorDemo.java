package com.msr.study.concurrent.atomics;

import java.util.concurrent.atomic.LongAccumulator;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-24 21:22:54
 */
public class LongAccumulatorDemo {
    public static void main(String[] args) {
        LongAccumulator accumulator = new LongAccumulator(Long::sum, 0);
        accumulator.accumulate(1);
        accumulator.accumulate(10);
        accumulator.accumulate(-4);
        System.out.println(accumulator.getThenReset());
        System.out.println(accumulator.get());
    }
}
