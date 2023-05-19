package de.maxhenkel.voicechat.extensions;

import net.minecraft.src.Packet;

public interface NetHandlerExtension {
    void processCustomPayload(Packet packet);
}
