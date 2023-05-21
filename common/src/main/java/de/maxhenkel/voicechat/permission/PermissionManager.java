package de.maxhenkel.voicechat.permission;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionManager {

    public static PermissionManager INSTANCE = CommonCompatibilityManager.INSTANCE.createPermissionManager();

    public final Permission LISTEN_PERMISSION;
    public final Permission SPEAK_PERMISSION;
    public final Permission GROUPS_PERMISSION;
    public final Permission ADMIN_PERMISSION;

    protected List<Permission> permissions = new ArrayList<>();

    public PermissionManager() {
        LISTEN_PERMISSION = createPermission(Voicechat.MODID, "listen", PermissionType.EVERYONE);
        SPEAK_PERMISSION = createPermission(Voicechat.MODID, "speak", PermissionType.EVERYONE);
        GROUPS_PERMISSION = createPermission(Voicechat.MODID, "groups", PermissionType.EVERYONE);
        ADMIN_PERMISSION = createPermission(Voicechat.MODID, "admin", PermissionType.OPS);
    }

    public abstract Permission createPermissionInternal(String modId, String node, PermissionType type);

    public Permission createPermission(String modId, String node, PermissionType type) {
        Permission p = createPermissionInternal(modId, node, type);
        permissions.add(p);
        return p;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }
}
