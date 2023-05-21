package de.maxhenkel.voicechat.voice.server;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class ServerWorldUtils {

    public static Collection<EntityPlayer> getPlayersInRange(World level, Vec3D pos, double range, Predicate<EntityPlayer> filter) {
        List<EntityPlayer> nearbyPlayers = new ArrayList<>();
        List<EntityPlayer> players = level.playerEntities;
        for (int i = 0; i < players.size(); i++) {
            EntityPlayer player = (EntityPlayer) players.get(i);
            if (isInRange(Vec3D.createVector(player.posX, player.posY, player.posZ), pos, range) && filter.test(player)) {
                nearbyPlayers.add(player);
            }
        }
        return nearbyPlayers;
    }

    public static boolean isInRange(Vec3D pos1, Vec3D pos2, double range) {
        return Math.abs(pos1.xCoord - pos2.xCoord) <= range && Math.abs(pos1.yCoord - pos2.yCoord) <= range && Math.abs(pos1.zCoord - pos2.zCoord) <= range;
    }

}
