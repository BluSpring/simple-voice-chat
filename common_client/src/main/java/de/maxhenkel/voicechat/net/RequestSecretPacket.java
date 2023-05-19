package de.maxhenkel.voicechat.net;

import de.maxhenkel.voicechat.util.ConnectionUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RequestSecretPacket implements Packet<RequestSecretPacket> {

    public static final String REQUEST_SECRET = ConnectionUtil.format(NetManager.CHANNEL, "request_secret");

    private int compatibilityVersion;

    public RequestSecretPacket() {

    }

    public RequestSecretPacket(int compatibilityVersion) {
        this.compatibilityVersion = compatibilityVersion;
    }

    public int getCompatibilityVersion() {
        return compatibilityVersion;
    }

    @Override
    public String getIdentifier() {
        return REQUEST_SECRET;
    }

    @Override
    public RequestSecretPacket fromBytes(DataInputStream buf) throws IOException {
        compatibilityVersion = buf.readInt();
        return this;
    }

    @Override
    public void toBytes(DataOutputStream buf) throws IOException {
        buf.writeInt(compatibilityVersion);
    }

}
