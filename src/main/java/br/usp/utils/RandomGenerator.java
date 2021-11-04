package br.usp.utils;

import java.util.Random;

public class RandomGenerator {
    public static int random(int n) {
        return new Random().nextInt(n);
    }
}
