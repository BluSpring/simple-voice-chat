package de.maxhenkel.voicechat.gui.audiodevice;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.client.AudioChannelConfig;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import de.maxhenkel.voicechat.voice.client.DataLines;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;

import javax.annotation.Nullable;
import java.util.List;

public class SelectSpeakerScreen extends SelectDeviceScreen {

    protected static final String SPEAKER_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/speaker.png");
    protected static final String TITLE = StringTranslate.getInstance().translateKey("gui.voicechat.select_speaker.title");
    protected static final String NO_SPEAKER = StringTranslate.getInstance().translateKey("message.voicechat.no_speaker");

    public SelectSpeakerScreen(@Nullable GuiScreen parent) {
        super(TITLE, parent);
    }

    @Override
    public List<String> getDevices() {
        return DataLines.getSpeakerNames(AudioChannelConfig.STEREO_FORMAT);
        // return SoundManager.getAllSpeakers();
    }

    @Override
    public String getSelectedDevice() {
        return VoicechatClient.CLIENT_CONFIG.speaker.get();
    }

    @Override
    public String getIcon(String device) {
        return SPEAKER_ICON;
    }

    @Override
    public String getEmptyListComponent() {
        return NO_SPEAKER;
    }

    @Override
    public String getVisibleName(String device) {
        return device;
        // return SoundManager.cleanDeviceName(device);
    }

    @Override
    public void onSelect(String device) {
        VoicechatClient.CLIENT_CONFIG.speaker.set(device).save();
        ClientVoicechat client = ClientManager.getClient();
        if (client != null) {
            client.reloadAudio();
        }
    }
}
