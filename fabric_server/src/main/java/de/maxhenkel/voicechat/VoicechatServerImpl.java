package de.maxhenkel.voicechat;

import de.maxhenkel.voicechat.extensions.EntityPlayerExtension;
import de.maxhenkel.voicechat.mixin.NetworkListenThreadAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class VoicechatServerImpl implements VoicechatServer {
    public static VoicechatServerImpl instance;

    private final MinecraftServer server;
    private final HashMap<UUID, String> uuidToUsernameMap = new HashMap<>();

    public VoicechatServerImpl(MinecraftServer server) {
        instance = this;
        this.server = server;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    @Override
    public void sendToPlayer(EntityPlayer player, Packet packet) {
        server.configManager.sendPacketToPlayer(player.username, packet);
    }

    @Override
    public EntityPlayer getPlayerByName(String name) {
        return server.configManager.getPlayerEntity(name);
    }

    @Override
    public EntityPlayer getPlayerByUuid(UUID uuid) {
        if (uuidToUsernameMap.containsKey(uuid))
            return getPlayerByName(uuidToUsernameMap.get(uuid));

        EntityPlayer player = null;
        for (EntityPlayer playerEntity : (List<EntityPlayer>) server.configManager.playerEntities) {
            if (((EntityPlayerExtension) playerEntity).getUniqueID().equals(uuid)) {
                player = playerEntity;
                break;
            }
        }

        if (player == null)
            return null;

        uuidToUsernameMap.put(uuid, player.username);
        return player;
    }

    @Override
    public List<EntityPlayer> getPlayerList() {
        return server.configManager.playerEntities;
    }

    @Override
    public String getServerIp() {
        return ((NetworkListenThreadAccessor) server.field_6036_c).getServerSocket().getInetAddress().getHostAddress();
    }
}
