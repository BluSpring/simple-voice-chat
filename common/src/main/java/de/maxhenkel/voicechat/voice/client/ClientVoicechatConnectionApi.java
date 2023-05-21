package de.maxhenkel.voicechat.voice.client;

import java.net.InetAddress;

public interface ClientVoicechatConnectionApi {
    InitializationDataApi getData();
    InetAddress getAddress();
}
