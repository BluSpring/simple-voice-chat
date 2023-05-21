package de.maxhenkel.voicechat.util;

public class MathHelper2 {
    public static byte clamp(byte b, byte c, byte d) {
        if (b < c) {
            return c;
        }
        if (b > d) {
            return d;
        }
        return b;
    }

    public static int clamp(int i, int j, int k) {
        if (i < j) {
            return j;
        }
        if (i > k) {
            return k;
        }
        return i;
    }

    public static long clamp(long l, long m, long n) {
        if (l < m) {
            return m;
        }
        if (l > n) {
            return n;
        }
        return l;
    }

    public static float clamp(float f, float g, float h) {
        if (f < g) {
            return g;
        }
        if (f > h) {
            return h;
        }
        return f;
    }

    public static double clamp(double d, double e, double f) {
        if (d < e) {
            return e;
        }
        if (d > f) {
            return f;
        }
        return d;
    }
}
