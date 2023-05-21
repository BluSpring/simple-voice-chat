package de.maxhenkel.voicechat.util;

public class PlatformUtils {
    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}
