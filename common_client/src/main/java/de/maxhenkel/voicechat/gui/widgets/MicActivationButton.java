package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.MicrophoneActivationType;
import net.minecraft.src.StringTranslate;

public class MicActivationButton extends EnumButton<MicrophoneActivationType> {

    private final VoiceActivationSlider voiceActivationSlider;

    public MicActivationButton(int id, int xIn, int yIn, int widthIn, int heightIn, VoiceActivationSlider voiceActivationSlider) {
        super(id, xIn, yIn, widthIn, heightIn, VoicechatClient.CLIENT_CONFIG.microphoneActivationType);
        this.voiceActivationSlider = voiceActivationSlider;
        updateText();
        setVisibility();
    }

    @Override
    protected String getText(MicrophoneActivationType type) {
        return String.format(StringTranslate.getInstance().translateKey("message.voicechat.activation_type"), type.getText());
    }

    @Override
    protected void onUpdate(MicrophoneActivationType type) {
        setVisibility();
    }

    private void setVisibility() {
        voiceActivationSlider.enabled2 = MicrophoneActivationType.VOICE.equals(entry.get());
    }

}
