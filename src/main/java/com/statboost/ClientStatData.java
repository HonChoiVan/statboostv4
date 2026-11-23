package com.statboost;

import java.util.Arrays;

/** Plain data holder (no client-only imports) so it is safe to touch from packet handlers. */
public class ClientStatData {
    public static int[] points = new int[Stat.values().length];
    public static int[] costs = new int[Stat.values().length];
    public static int max = 50;
    public static float percent = 4f;
    public static float maxBonus = 200f;

    public static void update(int[] p, int[] c, int m, float pc, float mb) {
        points = Arrays.copyOf(p, Stat.values().length);
        costs = Arrays.copyOf(c, Stat.values().length);
        max = m;
        percent = pc;
        maxBonus = mb;
    }
}
