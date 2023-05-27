package de.maxhenkel.voicechat.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.util.ConnectionUtil;
import net.minecraft.client.Minecraft;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameProfileUtils {

    private static final Minecraft mc = MinecraftAccessor.getMinecraft();

    public static String getSkinUrl(String username) {
        String data = ConnectionUtil.readText("https://api.mojang.com/users/profiles/minecraft/" + username);
        if (data == null)
            return null;

        JsonObject uuidData = JsonParser.parseString(data).getAsJsonObject();

        String skinData = ConnectionUtil.readText("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidData.get("id").getAsString());
        if (skinData == null)
            return null;

        JsonObject skinJson = JsonParser.parseString(skinData).getAsJsonObject();

        if (!skinJson.has("properties"))
            return null;

        JsonArray skinProperties = skinJson.getAsJsonArray("properties");

        if (skinProperties.isEmpty())
            return null;

        String b64str = skinProperties.get(0).getAsJsonObject().get("value").getAsString();
        String decoded = new String(Base64.getDecoder().decode(b64str));

        JsonObject propertyJson = JsonParser.parseString(decoded).getAsJsonObject();

        if (!propertyJson.has("textures"))
            return null;

        JsonObject texturesJson = propertyJson.getAsJsonObject("textures");

        if (!texturesJson.has("SKIN"))
            return null;

        return texturesJson.getAsJsonObject("SKIN").get("url").getAsString();
    }

    private static final Map<String, Integer> usernameToIdMap = new ConcurrentHashMap<>();

    public static void bindSkinTexture(String username) {
        int skinTextureId;

        if (!usernameToIdMap.containsKey(username)) {
            skinTextureId = mc.renderEngine.getTextureForDownloadableImage(getSkinUrl(username), null);
            usernameToIdMap.put(username, skinTextureId);
        } else {
            skinTextureId = usernameToIdMap.get(username);
        }

        if (skinTextureId >= 0) {
            mc.renderEngine.bindTexture(skinTextureId);
        } else {
            mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/mob/char.png"));
        }
    }

    /*public static ResourceLocation getSkin(UUID uuid) {
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection == null) {
            return DefaultPlayerSkin.getDefaultSkin(uuid);
        }
        NetworkPlayerInfo playerInfo = connection.getPlayerInfo(uuid);
        if (playerInfo == null) {
            return DefaultPlayerSkin.getDefaultSkin(uuid);
        }
        return playerInfo.getLocationSkin();
    }*/

}
