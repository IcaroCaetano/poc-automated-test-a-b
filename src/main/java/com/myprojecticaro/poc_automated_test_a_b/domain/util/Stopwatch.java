package com.myprojecticaro.poc_automated_test_a_b.domain.util;


/**
 * Utility class for measuring elapsed time with high precision.
 * <p>
 * The {@code Stopwatch} captures the moment of its instantiation and
 * provides the elapsed time in microseconds since creation.
 * <p>
 * This class is immutable and thread-safe.
 *
 * <pre>
 * Example usage:
 *
 * Stopwatch stopwatch = new Stopwatch();
 * // ... some code execution ...
 * long elapsedMicros = stopwatch.micros();
 * System.out.println("Elapsed time (µs): " + elapsedMicros);
 * </pre>
 *
 * @author Icaro
 * @since 1.0
 */
public final class Stopwatch {

    /**
     * The start time in nanoseconds, captured at the moment of instantiation.
     */
    private final long start = System.nanoTime();

    /**
     * Returns the elapsed time in microseconds since the creation of this {@code Stopwatch}.
     *
     * @return the elapsed time in microseconds
     */
    public long micros() { return (System.nanoTime() - start) / 1_000; }
}
