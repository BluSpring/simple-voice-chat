package de.maxhenkel.voicechat.voice.client;

import net.minecraft.src.StringTranslate;

public enum MicrophoneActivationType {

    PTT(StringTranslate.getInstance().translateKey("message.voicechat.activation_type.ptt")), VOICE(StringTranslate.getInstance().translateKey("message.voicechat.activation_type.voice"));

    private final String component;

    MicrophoneActivationType(String component) {
        this.component = component;
    }

    public String getText() {
        return component;
    }
}
