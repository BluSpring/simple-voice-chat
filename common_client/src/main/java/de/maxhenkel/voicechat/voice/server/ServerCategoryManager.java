package de.maxhenkel.voicechat.voice.server;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
import de.maxhenkel.voicechat.net.AddCategoryPacket;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.net.RemoveCategoryPacket;
import de.maxhenkel.voicechat.plugins.CategoryManager;
import de.maxhenkel.voicechat.plugins.impl.VolumeCategoryImpl;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

public class ServerCategoryManager extends CategoryManager {

    private final Server server;

    public ServerCategoryManager(Server server) {
        this.server = server;
        CommonCompatibilityManager.INSTANCE.onPlayerCompatibilityCheckSucceeded(this::onPlayerCompatibilityCheckSucceeded);
    }

    private void onPlayerCompatibilityCheckSucceeded(EntityPlayerMP player) {
        Voicechat.logDebug("Synchronizing {} volume categories with {}", categories.size(), player.getDisplayNameString());
        for (VolumeCategoryImpl category : getCategories()) {
            broadcastAddCategory(server.getServer(), category);
        }
    }

    @Override
    public void addCategory(VolumeCategoryImpl category) {
        super.addCategory(category);
        Voicechat.logDebug("Synchronizing volume category {} with all players", category.getId());
        broadcastAddCategory(server.getServer(), category);
    }

    @Override
    @Nullable
    public VolumeCategoryImpl removeCategory(String categoryId) {
        VolumeCategoryImpl volumeCategory = super.removeCategory(categoryId);
        Voicechat.logDebug("Removing volume category {} for all players", categoryId);
        broadcastRemoveCategory(server.getServer(), categoryId);
        return volumeCategory;
    }

    private void broadcastAddCategory(MinecraftServer server, VolumeCategoryImpl category) {
        AddCategoryPacket packet = new AddCategoryPacket(category);
        server.getPlayerList().getPlayers().forEach(p -> NetManager.sendToClient(p, packet));
    }

    private void broadcastRemoveCategory(MinecraftServer server, String categoryId) {
        RemoveCategoryPacket packet = new RemoveCategoryPacket(categoryId);
        server.getPlayerList().getPlayers().forEach(p -> NetManager.sendToClient(p, packet));
    }

}
