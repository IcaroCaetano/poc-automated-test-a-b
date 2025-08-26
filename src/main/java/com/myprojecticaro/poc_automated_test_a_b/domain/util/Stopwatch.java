package com.myprojecticaro.poc_automated_test_a_b.domain.util;

public final class Stopwatch {
    private final long start = System.nanoTime();
    public long micros() { return (System.nanoTime() - start) / 1_000; }
}