package de.maxhenkel.voicechat.intercompatibility;

import de.maxhenkel.voicechat.VoicechatServerImpl;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.net.FabricNetManager;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.permission.NoOpPermissionManager;
import de.maxhenkel.voicechat.permission.PermissionManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.EntityPlayer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FabricCommonCompatibilityManager extends CommonCompatibilityManager {
    public static FabricCommonCompatibilityManager instance;

    private final List<Consumer<Object>> serverStartingEvents;
    private final List<Consumer<Object>> serverStoppingEvents;
    private final List<Consumer<EntityPlayer>> playerLoggedInEvents;
    private final List<Consumer<EntityPlayer>> playerLoggedOutEvents;
    private final List<Consumer<EntityPlayer>> voicechatConnectEvents;
    private final List<Consumer<EntityPlayer>> voicechatCompatibilityCheckSucceededEvents;
    private final List<Consumer<UUID>> voicechatDisconnectEvents;

    public FabricCommonCompatibilityManager() {
        instance = this;

        serverStartingEvents = new ArrayList<>();
        serverStoppingEvents = new ArrayList<>();
        playerLoggedInEvents = new ArrayList<>();
        playerLoggedOutEvents = new ArrayList<>();
        voicechatConnectEvents = new ArrayList<>();
        voicechatCompatibilityCheckSucceededEvents = new ArrayList<>();
        voicechatDisconnectEvents = new ArrayList<>();
    }

    public void onServerStart() {
        this.serverStartingEvents.forEach(a -> a.accept(VoicechatServerImpl.instance.getServer()));
    }

    public void onServerStop() {
        this.serverStoppingEvents.forEach(a -> a.accept(VoicechatServerImpl.instance.getServer()));
    }

    public void onPlayerLogIn(EntityPlayer player) {
        this.playerLoggedInEvents.forEach(a -> a.accept(player));
    }

    public void onPlayerLogOut(EntityPlayer player) {
        this.playerLoggedOutEvents.forEach(a -> a.accept(player));
    }

    @Override
    public String getModVersion() {
        return FabricLoader.getInstance().getModContainer("voicechat").get().getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public String getModName() {
        return FabricLoader.getInstance().getModContainer("voicechat").get().getMetadata().getName();
    }

    @Override
    public Path getGameDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public void emitServerVoiceChatConnectedEvent(EntityPlayer player) {
        voicechatConnectEvents.forEach(consumer -> consumer.accept(player));
    }

    @Override
    public void emitServerVoiceChatDisconnectedEvent(UUID clientID) {
        voicechatDisconnectEvents.forEach(consumer -> consumer.accept(clientID));
    }

    @Override
    public void emitPlayerCompatibilityCheckSucceeded(EntityPlayer player) {
        voicechatCompatibilityCheckSucceededEvents.forEach(consumer -> consumer.accept(player));
    }

    @Override
    public void onServerVoiceChatConnected(Consumer<EntityPlayer> onVoiceChatConnected) {
        voicechatConnectEvents.add(onVoiceChatConnected);
    }

    @Override
    public void onServerVoiceChatDisconnected(Consumer<UUID> onVoiceChatDisconnected) {
        voicechatDisconnectEvents.add(onVoiceChatDisconnected);
    }

    @Override
    public void onServerStarting(Consumer<Object> onServerStarting) {
        serverStartingEvents.add(onServerStarting);
    }

    @Override
    public void onServerStopping(Consumer<Object> onServerStopping) {
        serverStoppingEvents.add(onServerStopping);
    }

    @Override
    public void onPlayerLoggedIn(Consumer<EntityPlayer> onPlayerLoggedIn) {
        playerLoggedInEvents.add(onPlayerLoggedIn);
    }

    @Override
    public void onPlayerLoggedOut(Consumer<EntityPlayer> onPlayerLoggedOut) {
        playerLoggedOutEvents.add(onPlayerLoggedOut);
    }

    @Override
    public void onPlayerCompatibilityCheckSucceeded(Consumer<EntityPlayer> onPlayerCompatibilityCheckSucceeded) {
        voicechatCompatibilityCheckSucceededEvents.add(onPlayerCompatibilityCheckSucceeded);
    }

    private FabricNetManager netManager;

    @Override
    public NetManager getNetManager() {
        if (netManager == null)
            netManager = new FabricNetManager();

        return netManager;
    }

    @Override
    public boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public boolean isDedicatedServer() {
        try {
            Class.forName("net.minecraft.client.Minecraft");
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public List<VoicechatPlugin> loadPlugins() {
        return new LinkedList<>();
    }

    @Override
    public PermissionManager createPermissionManager() {
        return new NoOpPermissionManager();
    }
}
