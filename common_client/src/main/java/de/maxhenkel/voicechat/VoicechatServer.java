package de.maxhenkel.voicechat;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet;

import java.util.List;
import java.util.UUID;

public interface VoicechatServer {
    void sendToPlayer(EntityPlayer player, Packet packet);
    EntityPlayer getPlayerByName(String name);
    EntityPlayer getPlayerByUuid(UUID uuid);
    List<EntityPlayer> getPlayerList();
    String getServerIp();
}
