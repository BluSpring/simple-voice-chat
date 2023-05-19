package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.VoicechatClient;
import net.minecraft.src.StringTranslate;

public class MicAmplificationSlider extends DebouncedSlider {

    private static final float MAXIMUM = 4F;

    public MicAmplificationSlider(int id, int xIn, int yIn, int widthIn, int heightIn) {
        super(id, xIn, yIn, widthIn, heightIn, VoicechatClient.CLIENT_CONFIG.microphoneAmplification.get().floatValue() / MAXIMUM);
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        long amp = Math.round(value * MAXIMUM * 100F - 100F);
        displayString = String.format(StringTranslate.getInstance().translateKey("message.voicechat.microphone_amplification"), (amp > 0F ? "+" : "") + amp + "%");
    }

    @Override
    public void applyDebounced() {
        VoicechatClient.CLIENT_CONFIG.microphoneAmplification.set(value * MAXIMUM).save();
    }
}
