package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.api.Group;
import net.minecraft.src.StringTranslate;

public enum GroupType {
    NORMAL(StringTranslate.getInstance().translateKey("message.voicechat.group_type.normal"), StringTranslate.getInstance().translateKey("message.voicechat.group_type.normal.description"), Group.Type.NORMAL),
    OPEN(StringTranslate.getInstance().translateKey("message.voicechat.group_type.open"), StringTranslate.getInstance().translateKey("message.voicechat.group_type.open.description"), Group.Type.OPEN),
    ISOLATED(StringTranslate.getInstance().translateKey("message.voicechat.group_type.isolated"), StringTranslate.getInstance().translateKey("message.voicechat.group_type.isolated.description"), Group.Type.ISOLATED);

    private final String translation;
    private final String description;
    private final Group.Type type;

    GroupType(String translation, String description, Group.Type type) {
        this.translation = translation;
        this.description = description;
        this.type = type;
    }

    public String getTranslation() {
        return translation;
    }

    public String getDescription() {
        return description;
    }

    public Group.Type getType() {
        return type;
    }

    public static GroupType fromType(Group.Type type) {
        for (GroupType groupType : values()) {
            if (groupType.getType() == type) {
                return groupType;
            }
        }
        return NORMAL;
    }

}
