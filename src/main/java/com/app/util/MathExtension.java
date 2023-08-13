package com.app.util;

/**
 * This class provides various utility methods for mathematical calculations and conversions.
 */
public class MathExtension {

    /**
     * Calculates the factorial of a non-negative integer.
     *
     * @param n The non-negative integer for which to calculate the factorial.
     * @return The factorial of the given integer.
     * @throws IllegalArgumentException If the input is negative.
     */
    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    /**
     * Calculates the exponential value of a base raised to a given exponent.
     *
     * @param base     The base value.
     * @param exponent The exponent.
     * @return The result of the base raised to the exponent.
     */
    public static double exponential(double base, int exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * Calculates the nth root of a given value.
     *
     * @param value The value for which to calculate the nth root.
     * @param n     The root value (should be greater than 0).
     * @return The nth root of the value.
     * @throws IllegalArgumentException If n is 0.
     */
    public static double nthRoot(double value, int n) {
        if (n == 0) {
            throw new IllegalArgumentException("Cannot calculate 0th root.");
        }
        return Math.pow(value, 1.0 / n);
    }

    /**
     * Calculates the average of an array of values.
     *
     * @param values The values for which to calculate the average.
     * @return The average of the provided values.
     */
    public static double average(double... values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    /**
     * Calculates the greatest common divisor (GCD) of two integers using the Euclidean algorithm.
     *
     * @param a The first integer.
     * @param b The second integer.
     * @return The greatest common divisor of the two integers.
     */
    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    /**
     * Calculates the least common multiple (LCM) of two integers.
     *
     * @param a The first integer.
     * @param b The second integer.
     * @return The least common multiple of the two integers.
     */
    public static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    /**
     * Converts degrees to radians.
     *
     * @param degrees The angle in degrees.
     * @return The equivalent angle in radians.
     */
    public static double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    /**
     * Converts radians to degrees.
     *
     * @param radians The angle in radians.
     * @return The equivalent angle in degrees.
     */
    public static double radiansToDegrees(double radians) {
        return radians * 180 / Math.PI;
    }

    /**
     * Checks if an integer is a power of two.
     *
     * @param n The integer to check.
     * @return {@code true} if the integer is a power of two, {@code false} otherwise.
     */
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * Rounds a value to the nearest multiple of a specified number.
     *
     * @param value    The value to round.
     * @param multiple The multiple to which the value should be rounded.
     * @return The value rounded to the nearest multiple.
     */
    public static int roundToNearestMultiple(int value, int multiple) {
        return Math.round((float) value / multiple) * multiple;
    }

    /**
     * Clamps an integer value to a specified range.
     *
     * @param value The value to be clamped.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @return The clamped value within the specified range.
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Clamps a long integer value to a specified range.
     *
     * @param value The value to be clamped.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @return The clamped value within the specified range.
     */
    public static long clamp(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Clamps a floating-point value to a specified range.
     *
     * @param value The value to be clamped.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @return The clamped value within the specified range.
     */
    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Clamps a double-precision floating-point value to a specified range.
     *
     * @param value The value to be clamped.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @return The clamped value within the specified range.
     */
    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Clamps a byte value to a specified range.
     *
     * @param value The value to be clamped.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @return The clamped value within the specified range.
     */
    public static byte clamp(byte value, byte min, byte max) {
        return (byte) Math.min(Math.max(value, min), max);
    }

    /**
     * Clamps a short integer value to a specified range.
     *
     * @param value The value to be clamped.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @return The clamped value within the specified range.
     */
    public static short clamp(short value, short min, short max) {
        return (short) Math.min(Math.max(value, min), max);
    }
}
