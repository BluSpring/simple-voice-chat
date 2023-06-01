package de.maxhenkel.voicechat.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.maxhenkel.voicechat.Voicechat;
import net.minecraft.src.KeyBinding;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

public class KeyBindingHelper {
    private static final File SETTINGS_FILE = new File(Voicechat.getModConfigFolder().toFile(), "voicechat_keybinds.json");

    private static final List<KeyBinding> keyBindings = new LinkedList<>();

    public static List<KeyBinding> getKeyBindings() {
        return keyBindings;
    }

    public static void loadKeyBindings() {
        if (!SETTINGS_FILE.exists())
            return;

        try {
            String data = FileUtils.readFileToString(SETTINGS_FILE, Charset.defaultCharset());
            JsonObject obj = JsonParser.parseString(data).getAsJsonObject();

            for (KeyBinding keyBinding : keyBindings) {
                keyBinding.keyCode = obj.get(keyBinding.keyDescription).getAsInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveKeyBindings() {
        try {
            if (!SETTINGS_FILE.exists()) {
                if (!SETTINGS_FILE.getParentFile().exists()) {
                    SETTINGS_FILE.getParentFile().mkdirs();
                }

                SETTINGS_FILE.createNewFile();
            }

            JsonObject obj = new JsonObject();

            for (KeyBinding keyBinding : keyBindings) {
                obj.addProperty(keyBinding.keyDescription, keyBinding.keyCode);
            }

            FileUtils.write(SETTINGS_FILE, obj.toString(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerKeyBinding(KeyBinding keyBinding) {
        keyBindings.add(keyBinding);
    }
}
