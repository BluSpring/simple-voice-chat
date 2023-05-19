package de.maxhenkel.voicechat.gui.audiodevice;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.microphone.MicrophoneManager;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

import javax.annotation.Nullable;
import java.util.List;

public class SelectMicrophoneScreen extends SelectDeviceScreen {

    protected static final String MICROPHONE_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/microphone.png");
    protected static final String TITLE = StringTranslate.getInstance().translateKey("gui.voicechat.select_microphone.title");
    protected static final String NO_MICROPHONE = StringTranslate.getInstance().translateKey("message.voicechat.no_microphone");

    public SelectMicrophoneScreen(@Nullable GuiScreen parent) {
        super(TITLE, parent);
    }

    @Override
    public List<String> getDevices() {
        return MicrophoneManager.deviceNames();
    }

    @Override
    public String getSelectedDevice() {
        return VoicechatClient.CLIENT_CONFIG.microphone.get();
    }

    @Override
    public String getIcon(String device) {
        return MICROPHONE_ICON;
    }

    @Override
    public String getEmptyListComponent() {
        return NO_MICROPHONE;
    }

    @Override
    public String getVisibleName(String device) {
        // return SoundManager.cleanDeviceName(device);
        return device;
    }

    @Override
    public void onSelect(String device) {
        VoicechatClient.CLIENT_CONFIG.microphone.set(device).save();
        ClientVoicechat client = ClientManager.getClient();
        if (client != null) {
            client.reloadAudio();
        }
    }
}
