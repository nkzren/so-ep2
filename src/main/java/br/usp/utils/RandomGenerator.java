package br.usp.utils;

public class RandomGenerator {
    public static long random(int n) {
        return Math.round(Math.random()) * n;
    }
}
