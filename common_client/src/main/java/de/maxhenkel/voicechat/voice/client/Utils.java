package de.maxhenkel.voicechat.voice.client;

import net.minecraft.src.MathHelper;
import org.lwjgl.util.vector.Vector2f;

public class Utils extends de.maxhenkel.voicechat.voice.common.Utils {
    public static float angle(Vector2f vec1, Vector2f vec2) {
        return (float) Math.toDegrees(Math.atan2(vec1.x * vec2.x + vec1.y * vec2.y, vec1.x * vec2.y - vec1.y * vec2.x));
    }

    private static double magnitude(Vector2f vec1) {
        return Math.sqrt(Math.pow(vec1.x, 2) + Math.pow(vec1.y, 2));
    }

    private static float multiply(Vector2f vec1, Vector2f vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }

    private static Vector2f rotate(Vector2f vec, float angle) {
        return new Vector2f(vec.x * MathHelper.cos(angle) - vec.y * MathHelper.sin(angle), vec.x * MathHelper.sin(angle) + vec.y * MathHelper.cos(angle));
    }
}
