package de.maxhenkel.voicechat.permission;

import net.minecraft.src.EntityPlayer;

import javax.annotation.Nullable;

public enum PermissionType {

    EVERYONE, NOONE, OPS;

    boolean hasPermission(@Nullable EntityPlayer player) {
        switch (this) {
            case EVERYONE:
                return true;
            default:
            case NOONE:
                return false;
            case OPS:
                return false;
        }
    }

}
