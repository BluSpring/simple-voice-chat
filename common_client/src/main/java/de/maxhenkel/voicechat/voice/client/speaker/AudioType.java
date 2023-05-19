package de.maxhenkel.voicechat.voice.client.speaker;

import net.minecraft.src.StringTranslate;

public enum AudioType {

    NORMAL(StringTranslate.getInstance().translateKey("message.voicechat.audio_type.normal")), REDUCED(StringTranslate.getInstance().translateKey("message.voicechat.audio_type.reduced")), OFF(StringTranslate.getInstance().translateKey("message.voicechat.audio_type.off"));

    private final String component;

    AudioType(String component) {
        this.component = component;
    }

    public String getText() {
        return component;
    }
}
