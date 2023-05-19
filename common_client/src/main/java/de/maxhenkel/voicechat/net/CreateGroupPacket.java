package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.plugins.impl.GroupImpl;
import de.maxhenkel.voicechat.util.ConnectionUtil;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CreateGroupPacket implements Packet<CreateGroupPacket> {

    public static final String CREATE_GROUP = ConnectionUtil.format(NetManager.CHANNEL, "create_group");

    private String name;
    @Nullable
    private String password;
    private Group.Type type;

    public CreateGroupPacket() {

    }

    public CreateGroupPacket(String name, @Nullable String password, Group.Type type) {
        this.name = name;
        this.password = password;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public Group.Type getType() {
        return type;
    }

    @Override
    public String getIdentifier() {
        return CREATE_GROUP;
    }

    @Override
    public CreateGroupPacket fromBytes(DataInputStream buf) throws IOException {
        name = buf.readUTF();
        password = null;
        if (buf.readBoolean()) {
            password = buf.readUTF();
        }
        type = GroupImpl.TypeImpl.fromInt(buf.readShort());
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeUTF(name);
        buf.writeBoolean(password != null);
        if (password != null) {
            buf.writeUTF(password);
        }
        buf.writeShort(GroupImpl.TypeImpl.toInt(type));
    }

}
