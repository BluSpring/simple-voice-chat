package de.maxhenkel.voicechat.voice.common;

import javax.annotation.Nullable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PlayerState {

    private UUID uuid;
    private String name;
    private boolean disabled;
    private boolean disconnected;
    @Nullable
    private UUID group;

    public PlayerState(UUID uuid, String name, boolean disabled, boolean disconnected) {
        this.uuid = uuid;
        this.name = name;
        this.disabled = disabled;
        this.disconnected = disconnected;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    @Nullable
    public UUID getGroup() {
        return group;
    }

    public void setGroup(@Nullable UUID group) {
        this.group = group;
    }

    public boolean hasGroup() {
        return group != null;
    }

    @Override
    public String toString() {
        return "{" +
                "disabled=" + disabled +
                ", disconnected=" + disconnected +
                ", uuid=" + uuid +
                ", name=" + name +
                ", group=" + group +
                '}';
    }

    public static PlayerState fromBytes(DataInputStream buf) throws IOException {
        boolean disabled = buf.readBoolean();
        boolean disconnected = buf.readBoolean();
        UUID uuid = UUID.fromString(buf.readUTF());
        String name = buf.readUTF();

        PlayerState state = new PlayerState(uuid, name, disabled, disconnected);

        if (buf.readBoolean()) {
            state.setGroup(UUID.fromString(buf.readUTF()));
        }

        return state;
    }

    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeBoolean(disabled);
        buf.writeBoolean(disconnected);
        buf.writeUTF(uuid.toString());
        buf.writeUTF(name);
        buf.writeBoolean(hasGroup());
        if (hasGroup()) {
            buf.writeUTF(group.toString());
        }
    }

}
