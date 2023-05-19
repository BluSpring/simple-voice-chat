package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.common.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class VoiceActivationSlider extends DebouncedSlider implements MicTestButton.MicListener {

    private static final String SLIDER = TextureHelper.format(Voicechat.MODID, "textures/gui/voice_activation_slider.png");
    private static final String NO_ACTIVATION = "§c" + StringTranslate.getInstance().translateKey("message.voicechat.voice_activation.disabled");

    private double micValue;

    public VoiceActivationSlider(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height, Utils.dbToPerc(VoicechatClient.CLIENT_CONFIG.voiceActivationThreshold.get().floatValue()));
        updateMessage();
    }

    @Override
    public void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        super.mouseDragged(mc, mouseX, mouseY);
        TextureHelper.bindTexture(SLIDER);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        int width = (int) (226D * micValue);
        drawTexturedModalRect(xPosition + 1, yPosition + 1, 0, 0, width, 18);
    }

    @Override
    protected void updateMessage() {
        long db = Math.round(Utils.percToDb(value));
        String component = String.format(StringTranslate.getInstance().translateKey("message.voicechat.voice_activation"), db);

        if (db >= -10L) {
            component = "§c" + component;
        }

        displayString = component;
    }

    @Nullable
    public String getTooltip() {
        if (value >= 1D) {
            return NO_ACTIVATION;
        }
        return null;
    }

    @Override
    public void applyDebounced() {
        VoicechatClient.CLIENT_CONFIG.voiceActivationThreshold.set(Utils.percToDb(value)).save();
    }

    @Override
    public void onMicValue(double percentage) {
        this.micValue = percentage;
    }
}
