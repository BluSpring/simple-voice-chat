package de.maxhenkel.voicechat.voice.client.speaker;

import de.maxhenkel.voicechat.voice.client.PositionalAudioUtils;
import de.maxhenkel.voicechat.voice.client.SoundManager;
import net.minecraft.src.Vec3D;
import org.lwjgl.openal.AL10;

import javax.annotation.Nullable;
import java.nio.ShortBuffer;
import java.util.UUID;

@Deprecated
public class MonoALSpeaker extends ALSpeakerBase {

    public MonoALSpeaker(SoundManager soundManager, int sampleRate, int bufferSize, @Nullable UUID audioChannelId) {
        super(soundManager, sampleRate, bufferSize, audioChannelId);
    }

    @Override
    protected void openSync() {
        super.openSync();
        AL10.alDistanceModel(AL10.AL_NONE);
        SoundManager.checkAlError();
    }

    @Override
    protected int getFormat() {
        return AL10.AL_FORMAT_MONO16;
    }

    @Override
    protected void setPositionSync(@Nullable Vec3D soundPos, float maxDistance) {

    }

    @Override
    protected ShortBuffer convert(short[] data, @Nullable Vec3D position) {
        return toShortBuffer(data);
    }

    @Override
    protected float getVolume(float volume, @Nullable Vec3D position, float maxDistance) {
        if (position == null) {
            return super.getVolume(volume, position, maxDistance);
        }
        return super.getVolume(volume, position, maxDistance) * PositionalAudioUtils.getDistanceVolume(maxDistance, position);
    }


}
