package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.util.ConnectionUtil;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class JoinGroupPacket implements Packet<JoinGroupPacket> {

    public static final String SET_GROUP = ConnectionUtil.format(NetManager.CHANNEL, "set_group");

    private UUID group;
    @Nullable
    private String password;

    public JoinGroupPacket() {

    }

    public JoinGroupPacket(UUID group, @Nullable String password) {
        this.group = group;
        this.password = password;
    }

    public UUID getGroup() {
        return group;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    @Override
    public String getIdentifier() {
        return SET_GROUP;
    }

    @Override
    public JoinGroupPacket fromBytes(DataInputStream buf) throws IOException {
        group = UUID.fromString(buf.readUTF());
        if (buf.readBoolean()) {
            password = buf.readUTF();
        }
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeUTF(group.toString());
        buf.writeBoolean(password != null);
        if (password != null) {
            buf.writeUTF(password);
        }
    }

}
