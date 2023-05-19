package de.maxhenkel.voicechat.permission;

import net.minecraft.src.EntityPlayer;

public class NoOpPermissionManager extends PermissionManager {
    private static final Permission NO_OP = new NoOpPermission();

    @Override
    public Permission createPermissionInternal(String modId, String node, PermissionType type) {
        return NO_OP;
    }

    public static class NoOpPermission implements Permission {

        @Override
        public boolean hasPermission(EntityPlayer player) {
            return false;
        }

        @Override
        public PermissionType getPermissionType() {
            return PermissionType.NOONE;
        }
    }
}
