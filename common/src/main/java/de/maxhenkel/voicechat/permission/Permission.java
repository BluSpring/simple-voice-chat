package de.maxhenkel.voicechat.permission;

import net.minecraft.src.EntityPlayer;

public interface Permission {

    boolean hasPermission(EntityPlayer player);

    PermissionType getPermissionType();

}
