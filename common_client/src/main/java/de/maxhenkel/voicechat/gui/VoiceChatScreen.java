package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.gui.group.GroupScreen;
import de.maxhenkel.voicechat.gui.group.JoinGroupScreen;
import de.maxhenkel.voicechat.gui.tooltips.DisableTooltipSupplier;
import de.maxhenkel.voicechat.gui.tooltips.HideTooltipSupplier;
import de.maxhenkel.voicechat.gui.tooltips.MuteTooltipSupplier;
import de.maxhenkel.voicechat.gui.tooltips.RecordingTooltipSupplier;
import de.maxhenkel.voicechat.gui.volume.AdjustVolumesScreen;
import de.maxhenkel.voicechat.gui.widgets.ButtonBase;
import de.maxhenkel.voicechat.gui.widgets.ImageButton;
import de.maxhenkel.voicechat.gui.widgets.ToggleImageButton;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.*;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.io.IOException;

public class VoiceChatScreen extends VoiceChatScreenBase {

    private static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_voicechat.png");
    private static final String MICROPHONE = TextureHelper.format(Voicechat.MODID, "textures/icons/microphone_button.png");
    private static final String HIDE = TextureHelper.format(Voicechat.MODID, "textures/icons/hide_button.png");
    private static final String VOLUMES = TextureHelper.format(Voicechat.MODID, "textures/icons/adjust_volumes.png");
    private static final String SPEAKER = TextureHelper.format(Voicechat.MODID, "textures/icons/speaker_button.png");
    private static final String RECORD = TextureHelper.format(Voicechat.MODID, "textures/icons/record_button.png");
    private static final String TITLE = StringTranslate.getInstance().translateKey("gui.voicechat.voice_chat.title");
    private static final String SETTINGS = StringTranslate.getInstance().translateKey("message.voicechat.settings");
    private static final String GROUP = StringTranslate.getInstance().translateKey("message.voicechat.group");
    private static final String ADJUST_PLAYER_VOLUMES = StringTranslate.getInstance().translateKey("message.voicechat.adjust_volumes");

    private ToggleImageButton mute;
    private ToggleImageButton disable;
    private HoverArea recordingHoverArea;

    private ClientPlayerStateManager stateManager;

    public VoiceChatScreen() {
        super(TITLE, 195, 76);
        stateManager = ClientManager.getPlayerStateManager();
    }

    @Override
    public void initGui() {
        super.initGui();
        @Nullable ClientVoicechat client = ClientManager.getClient();

        mute = new ToggleImageButton(0, guiLeft + 6, guiTop + ySize - 6 - 20, MICROPHONE, stateManager::isMuted, button -> {
            stateManager.setMuted(!stateManager.isMuted());
        }, new MuteTooltipSupplier(this, stateManager));
        controlList.add(mute);

        disable = new ToggleImageButton(1, guiLeft + 6 + 20 + 2, guiTop + ySize - 6 - 20, SPEAKER, stateManager::isDisabled, button -> {
            stateManager.setDisabled(!stateManager.isDisabled());
        }, new DisableTooltipSupplier(this, stateManager));
        controlList.add(disable);

        ImageButton volumes = new ImageButton(2, guiLeft + 6 + 20 + 2 + 20 + 2, guiTop + ySize - 6 - 20, VOLUMES, button -> {
            mc.displayGuiScreen(new AdjustVolumesScreen());
        }, (button, mouseX, mouseY) -> {
            drawHoveringText(ADJUST_PLAYER_VOLUMES, mouseX, mouseY);
        });
        controlList.add(volumes);

        if (client != null && VoicechatClient.CLIENT_CONFIG.useNatives.get()) {
            if (client.getRecorder() != null || (client.getConnection() != null && client.getConnection().getData().allowRecording())) {
                ToggleImageButton record = new ToggleImageButton(3, guiLeft + xSize - 6 - 20 - 2 - 20, guiTop + ySize - 6 - 20, RECORD, () -> ClientManager.getClient() != null && ClientManager.getClient().getRecorder() != null, button -> toggleRecording(), new RecordingTooltipSupplier(this));
                controlList.add(record);
            }
        }

        ToggleImageButton hide = new ToggleImageButton(4, guiLeft + xSize - 6 - 20, guiTop + ySize - 6 - 20, HIDE, VoicechatClient.CLIENT_CONFIG.hideIcons::get, button -> {
            VoicechatClient.CLIENT_CONFIG.hideIcons.set(!VoicechatClient.CLIENT_CONFIG.hideIcons.get()).save();
        }, new HideTooltipSupplier(this));
        controlList.add(hide);

        ButtonBase settings = new ButtonBase(5, guiLeft + 6, guiTop + 6 + 15, 75, 20, SETTINGS) {
            @Override
            public void onPress() {
                mc.displayGuiScreen(new VoiceChatSettingsScreen());
            }
        };
        controlList.add(settings);

        ButtonBase group = new ButtonBase(6, guiLeft + xSize - 6 - 75 + 1, guiTop + 6 + 15, 75, 20, GROUP) {
            @Override
            public void onPress() {
                ClientGroup g = stateManager.getGroup();
                if (g != null) {
                    mc.displayGuiScreen(new GroupScreen(g));
                } else {
                    mc.displayGuiScreen(new JoinGroupScreen());
                }
            }
        };
        controlList.add(group);
        group.enabled = client != null && client.getConnection() != null && client.getConnection().getData().groupsEnabled();
        recordingHoverArea = new HoverArea(6 + 20 + 2 + 20 + 2 + 20 + 2, ySize - 6 - 20, xSize - ((6 + 20 + 2 + 20 + 2) * 2 + 20 + 2), 20);

        checkButtons();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        checkButtons();
    }

    private void checkButtons() {
        if (mute != null) {
            mute.enabled = MuteTooltipSupplier.canMuteMic();
        }
        if (disable != null) {
            disable.enabled = stateManager.canEnable();
        }
    }

    private void toggleRecording() {
        ClientVoicechat c = ClientManager.getClient();
        if (c == null) {
            return;
        }
        c.toggleRecording();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == KeyEvents.KEY_VOICE_CHAT.keyCode) {
            mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void renderBackground(int mouseX, int mouseY, float delta) {
        GL11.glDisable(GL11.GL_SHADE_MODEL);
        TextureHelper.bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        GL11.glEnable(GL11.GL_SHADE_MODEL);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, float delta) {
        int titleWidth = fontRenderer.getStringWidth(TITLE);
        fontRenderer.drawString(TITLE, guiLeft + (xSize - titleWidth) / 2, guiTop + 7, FONT_COLOR);

        ClientVoicechat client = ClientManager.getClient();
        if (client != null && client.getRecorder() != null) {
            AudioRecorder recorder = client.getRecorder();
            String time = recorder.getDuration();
            fontRenderer.drawString("§4" + time, guiLeft + recordingHoverArea.getPosX() + recordingHoverArea.getWidth() / 2 - fontRenderer.getStringWidth(time) / 2, guiTop + recordingHoverArea.getPosY() + recordingHoverArea.getHeight() / 2 - (int)TextureHelper.FONT_HEIGHT / 2, 0);

            if (recordingHoverArea.isHovered(guiLeft, guiTop, mouseX, mouseY)) {
                drawHoveringText(String.format(StringTranslate.getInstance().translateKey("message.voicechat.storage_size"), recorder.getStorage()), mouseX, mouseY);
            }
        }
    }

}
