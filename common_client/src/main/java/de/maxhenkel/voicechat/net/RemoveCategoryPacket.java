package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.util.ConnectionUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RemoveCategoryPacket implements Packet<RemoveCategoryPacket> {

    public static final String REMOVE_CATEGORY = ConnectionUtil.format(NetManager.CHANNEL, "remove_category");

    private String categoryId;

    public RemoveCategoryPacket() {

    }

    public RemoveCategoryPacket(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public String getIdentifier() {
        return REMOVE_CATEGORY;
    }

    @Override
    public RemoveCategoryPacket fromBytes(DataInputStream buf) throws IOException {
        categoryId = buf.readUTF();
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeUTF(categoryId);
    }

}
