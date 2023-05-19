package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.audiodevice.SelectMicrophoneScreen;
import de.maxhenkel.voicechat.gui.audiodevice.SelectSpeakerScreen;
import de.maxhenkel.voicechat.gui.volume.AdjustVolumesScreen;
import de.maxhenkel.voicechat.gui.widgets.*;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.Denoiser;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import de.maxhenkel.voicechat.voice.client.speaker.AudioType;

import javax.annotation.Nullable;

public class VoiceChatSettingsScreen extends VoiceChatScreenBase {

    private static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_voicechat_settings.png");
    private static final String TITLE = StringTranslate.getInstance().translateKey("gui.voicechat.voice_chat_settings.title");
    private static final String ENABLED = StringTranslate.getInstance().translateKey("message.voicechat.enabled");
    private static final String DISABLED = StringTranslate.getInstance().translateKey("message.voicechat.disabled");
    private static final String ADJUST_VOLUMES = StringTranslate.getInstance().translateKey("message.voicechat.adjust_volumes");
    private static final String SELECT_MICROPHONE = StringTranslate.getInstance().translateKey("message.voicechat.select_microphone");
    private static final String SELECT_SPEAKER = StringTranslate.getInstance().translateKey("message.voicechat.select_speaker");
    private static final String BACK = StringTranslate.getInstance().translateKey("message.voicechat.back");

    @Nullable
    private final GuiScreen parent;
    private VoiceActivationSlider voiceActivationSlider;

    public VoiceChatSettingsScreen(@Nullable GuiScreen parent) {
        super(TITLE, 248, 219);
        this.parent = parent;
    }

    public VoiceChatSettingsScreen() {
        this(null);
    }

    @Override
    public void initGui() {
        super.initGui();

        int y = guiTop + 20;

        controlList.add(new VoiceSoundSlider(0, guiLeft + 10, y, xSize - 20, 20));
        y += 21;
        controlList.add(new MicAmplificationSlider(1, guiLeft + 10, y, xSize - 20, 20));
        y += 21;
        BooleanConfigButton denoiser = new BooleanConfigButton(2, guiLeft + 10, y, xSize - 20, 20, VoicechatClient.CLIENT_CONFIG.denoiser, enabled -> {
            return String.format(StringTranslate.getInstance().translateKey("message.voicechat.denoiser"), enabled ? ENABLED : DISABLED);
        });
        controlList.add(denoiser);

        if (Denoiser.createDenoiser() == null) {
            denoiser.enabled = false;
        }
        y += 21;

        voiceActivationSlider = new VoiceActivationSlider(3, guiLeft + 10, y + 21, xSize - 20, 20);

        controlList.add(new MicActivationButton(4, guiLeft + 10, y, xSize - 20, 20, voiceActivationSlider));
        y += 21;

        controlList.add(voiceActivationSlider);
        y += 21;

        MicTestButton micTestButton = new MicTestButton(5, guiLeft + 10, y, xSize - 20, 20, voiceActivationSlider);
        controlList.add(micTestButton);
        y += 21;

        controlList.add(new EnumButton<AudioType>(6, guiLeft + 10, y, xSize - 20, 20, VoicechatClient.CLIENT_CONFIG.audioType) {

            @Override
            protected String getText(AudioType type) {
                return String.format(StringTranslate.getInstance().translateKey("message.voicechat.audio_type"), type.getText());
            }

            @Override
            protected void onUpdate(AudioType type) {
                ClientVoicechat client = ClientManager.getClient();
                if (client != null) {
                    micTestButton.stop();
                    client.reloadAudio();
                }
            }
        });
        y += 21;
        if (isIngame()) {
            controlList.add(new ButtonBase(7, guiLeft + 10, y, xSize - 20, 20, ADJUST_VOLUMES) {
                @Override
                public void onPress() {
                    mc.displayGuiScreen(new AdjustVolumesScreen());
                }
            });
            y += 21;
        }
        controlList.add(new ButtonBase(8, guiLeft + 10, y, xSize / 2 - 15, 20, SELECT_MICROPHONE) {
            @Override
            public void onPress() {
                mc.displayGuiScreen(new SelectMicrophoneScreen(VoiceChatSettingsScreen.this));
            }
        });
        controlList.add(new ButtonBase(9, guiLeft + xSize / 2 + 6, y, xSize / 2 - 15, 20, SELECT_SPEAKER) {
            @Override
            public void onPress() {
                mc.displayGuiScreen(new SelectSpeakerScreen(VoiceChatSettingsScreen.this));
            }
        });
        y += 21;
        if (!isIngame() && parent != null) {
            controlList.add(new ButtonBase(10, guiLeft + 10, y, xSize - 20, 20, BACK) {
                @Override
                public void onPress() {
                    mc.displayGuiScreen(parent);
                }
            });
        }
    }

    @Override
    public void renderBackground(int mouseX, int mouseY, float delta) {
        TextureHelper.bindTexture(TEXTURE);
        if (isIngame()) {
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        }
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, float delta) {
        int titleWidth = fontRenderer.getStringWidth(TITLE);
        fontRenderer.drawString(TITLE, guiLeft + (xSize - titleWidth) / 2, guiTop + 7, getFontColor());

        if (voiceActivationSlider == null) {
            return;
        }

        /*String tooltip = voiceActivationSlider.getTooltip();
        if (tooltip != null && voiceActivationSlider.isMouseOver()) {
            drawHoveringText(tooltip, mouseX, mouseY);
        }*/
    }
}
