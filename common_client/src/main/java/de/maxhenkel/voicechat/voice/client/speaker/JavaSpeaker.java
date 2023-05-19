package de.maxhenkel.voicechat.voice.client.speaker;

import de.maxhenkel.voicechat.voice.client.PositionalAudioUtils;
import net.minecraft.src.Vec3D;

import javax.annotation.Nullable;

public class JavaSpeaker extends JavaSpeakerBase {

    @Override
    protected short[] convertToStereo(short[] data, @Nullable Vec3D position) {
        return PositionalAudioUtils.convertToStereo(data, position);
    }
}
