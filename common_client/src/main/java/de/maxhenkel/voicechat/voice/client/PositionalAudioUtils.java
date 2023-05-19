package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.speaker.AudioType;
import de.maxhenkel.voicechat.voice.common.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class PositionalAudioUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * @param cameraPos the position of the listener
     * @param yRot      the Y rotation of the listener
     * @param soundPos  the position of the sound
     * @return a float array of length 2, containing the left and right volume (0-1)
     */
    private static float[] getStereoVolume(Vec3d cameraPos, float yRot, Vec3d soundPos) {
        Vec3d d = soundPos.subtract(cameraPos).normalize();
        Vec2f diff = new Vec2f((float) d.x, (float) d.z);
        float diffAngle = Utils.angle(diff, new Vec2f(-1F, 0F));
        float angle = Utils.normalizeAngle(diffAngle - (yRot % 360F));
        float dif = (float) (Math.abs(cameraPos.y - soundPos.y) / 32);

        float rot = angle / 180F;
        float perc = rot;
        if (rot < -0.5F) {
            perc = -(0.5F + (rot + 0.5F));
        } else if (rot > 0.5F) {
            perc = 0.5F - (rot - 0.5F);
        }
        perc = perc * (1 - dif);

        float minVolume = 0.3F;

        float left = perc < 0F ? Math.abs(perc * 1.4F) + minVolume : minVolume;
        float right = perc >= 0F ? (perc * 1.4F) + minVolume : minVolume;

        float fill = 1F - Math.max(left, right);
        left += fill;
        right += fill;

        return new float[]{left, right};
    }

    /**
     * @param soundPos the position of the sound
     * @return a float array of length 2, containing the left and right volume (0-1)
     */
    private static float[] getStereoVolume(Vec3d soundPos) {
        return getStereoVolume(getCameraPosition(), mc.player != null ? mc.player.rotationYaw : 0F, soundPos);
    }

    /**
     * Gets the volume for the provided distance
     *
     * @param maxDistance the maximum distance of the sound
     * @param pos         the position of the audio
     * @return the resulting audio volume
     */
    public static float getDistanceVolume(float maxDistance, Vec3d pos) {
        return getDistanceVolume(maxDistance, getCameraPosition(), pos);
    }

    /**
     * Gets the volume for the provided distance
     *
     * @param maxDistance the maximum distance of the sound
     * @param listenerPos the position of the listener
     * @param pos         the position of the audio
     * @return the resulting audio volume
     */
    public static float getDistanceVolume(float maxDistance, Vec3d listenerPos, Vec3d pos) {
        float distance = (float) pos.distanceTo(listenerPos);
        distance = Math.min(distance, maxDistance);
        return (1F - distance / maxDistance);
    }

    /**
     * Converts 16 bit mono audio to stereo based on the sound position
     * This does not include the volume based on distance
     *
     * @param audio    the audio data
     * @param soundPos the position of the sound - Might be null in case of non-positional audio
     * @return the stereo audio data
     */
    public static short[] convertToStereo(short[] audio, @Nullable Vec3d soundPos) {
        if (soundPos == null) {
            return convertToStereo(audio);
        }
        return convertToStereo(audio, getStereoVolume(soundPos));
    }

    /**
     * @param audio     the audio data
     * @param cameraPos the position of the listener
     * @param yRot      the Y rotation of the listener
     * @param soundPos  the position of the sound - Might be null in case of non-positional audio
     * @return the stereo audio data
     */
    public static short[] convertToStereo(short[] audio, Vec3d cameraPos, float yRot, @Nullable Vec3d soundPos) {
        if (soundPos == null) {
            return convertToStereo(audio);
        }
        return convertToStereo(audio, getStereoVolume(cameraPos, yRot, soundPos));
    }

    /**
     * Converts 16 bit mono audio to stereo
     *
     * @param audio the audio data
     * @return the adjusted audio
     */
    public static short[] convertToStereo(short[] audio) {
        short[] stereo = new short[audio.length * 2];
        for (int i = 0; i < audio.length; i++) {
            stereo[i * 2] = audio[i];
            stereo[i * 2 + 1] = audio[i];
        }
        return stereo;
    }

    /**
     * Converts 16 bit mono audio to stereo
     *
     * @param audio       the audio data
     * @param volumeLeft  the volume modifier for the left audio
     * @param volumeRight the volume modifier for the right audio
     * @return the adjusted audio
     */
    private static short[] convertToStereo(short[] audio, float volumeLeft, float volumeRight) {
        short[] stereo = new short[audio.length * 2];
        for (int i = 0; i < audio.length; i++) {
            short left = (short) (audio[i] * volumeLeft);
            short right = (short) (audio[i] * volumeRight);
            stereo[i * 2] = left;
            stereo[i * 2 + 1] = right;
        }
        return stereo;
    }

    /**
     * Converts 16 bit mono audio to stereo
     *
     * @param audio   the audio data
     * @param volumes a float array of length 2 containing the left and right volume
     * @return the adjusted audio
     */
    private static short[] convertToStereo(short[] audio, float[] volumes) {
        return convertToStereo(audio, volumes[0], volumes[1]);
    }

    /**
     * Converts 16 bit mono audio to stereo
     *
     * @param audio  the audio data
     * @param volume the volume
     * @return the adjusted audio
     */
    public static short[] convertToStereo(short[] audio, float volume) {
        return convertToStereo(audio, volume, volume);
    }

    public static short[] convertToStereoForRecording(float maxDistance, Vec3d pos, short[] monoData) {
        return convertToStereoForRecording(maxDistance, getCameraPosition(), ActiveRenderInfo.getRotationXZ(), pos, monoData);
    }

    public static short[] convertToStereoForRecording(float maxDistance, Vec3d pos, short[] monoData, float volume) {
        return convertToStereoForRecording(maxDistance, getCameraPosition(), ActiveRenderInfo.getRotationXZ(), pos, monoData, volume);
    }

    public static short[] convertToStereoForRecording(float maxDistance, Vec3d cameraPos, float yRot, Vec3d pos, short[] monoData) {
        return convertToStereoForRecording(maxDistance, cameraPos, yRot, pos, monoData, 1F);
    }

    public static short[] convertToStereoForRecording(float maxDistance, Vec3d cameraPos, float yRot, Vec3d pos, short[] monoData, float volume) {
        float distanceVolume = getDistanceVolume(maxDistance, cameraPos, pos) * volume;
        if (!VoicechatClient.CLIENT_CONFIG.audioType.get().equals(AudioType.OFF)) {
            float[] stereoVolume = getStereoVolume(cameraPos, yRot, pos);
            return convertToStereo(monoData, distanceVolume * stereoVolume[0], distanceVolume * stereoVolume[1]);
        } else {
            return convertToStereo(monoData, distanceVolume, distanceVolume);
        }
    }

    public static Vec3d getCameraPosition() {
        return ActiveRenderInfo.getCameraPosition().add(mc.player == null ? Vec3d.ZERO : mc.player.getPositionVector());
    }

}
