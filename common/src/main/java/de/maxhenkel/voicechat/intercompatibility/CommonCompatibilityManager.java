package de.maxhenkel.voicechat.intercompatibility;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.permission.PermissionManager;
import de.maxhenkel.voicechat.service.Service;
import net.minecraft.src.EntityPlayer;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class CommonCompatibilityManager {

    public static CommonCompatibilityManager INSTANCE = Service.get(CommonCompatibilityManager.class);

    public abstract String getModVersion();

    public abstract String getModName();

    public abstract Path getGameDirectory();

    public abstract void emitServerVoiceChatConnectedEvent(EntityPlayer player);

    public abstract void emitServerVoiceChatDisconnectedEvent(UUID clientID);

    public abstract void emitPlayerCompatibilityCheckSucceeded(EntityPlayer player);

    public abstract void onServerVoiceChatConnected(Consumer<EntityPlayer> onVoiceChatConnected);

    public abstract void onServerVoiceChatDisconnected(Consumer<UUID> onVoiceChatDisconnected);

    public abstract void onServerStarting(Consumer<Object> onServerStarting);

    public abstract void onServerStopping(Consumer<Object> onServerStopping);

    public abstract void onPlayerLoggedIn(Consumer<EntityPlayer> onPlayerLoggedIn);

    public abstract void onPlayerLoggedOut(Consumer<EntityPlayer> onPlayerLoggedOut);

    public abstract void onPlayerCompatibilityCheckSucceeded(Consumer<EntityPlayer> onPlayerCompatibilityCheckSucceeded);

    public abstract NetManager getNetManager();

    public abstract boolean isDevEnvironment();

    public abstract boolean isDedicatedServer();

    public abstract List<VoicechatPlugin> loadPlugins();

    public abstract PermissionManager createPermissionManager();

}
