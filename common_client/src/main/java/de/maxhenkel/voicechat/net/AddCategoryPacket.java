package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.plugins.impl.VolumeCategoryImpl;
import de.maxhenkel.voicechat.util.ConnectionUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AddCategoryPacket implements Packet<AddCategoryPacket> {

    public static final String ADD_CATEGORY = ConnectionUtil.format(NetManager.CHANNEL, "add_category");

    private VolumeCategoryImpl category;

    public AddCategoryPacket() {

    }

    public AddCategoryPacket(VolumeCategoryImpl category) {
        this.category = category;
    }

    public VolumeCategoryImpl getCategory() {
        return category;
    }

    @Override
    public String getIdentifier() {
        return ADD_CATEGORY;
    }

    @Override
    public AddCategoryPacket fromBytes(DataInputStream buf) throws IOException {
        category = VolumeCategoryImpl.fromBytes(buf);
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        category.toBytes(buf);
    }

}
