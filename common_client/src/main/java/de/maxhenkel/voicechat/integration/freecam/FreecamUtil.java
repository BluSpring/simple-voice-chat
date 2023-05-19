package de.maxhenkel.voicechat.integration.freecam;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.PositionalAudioUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class FreecamUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * @return whether freecam is currently in use
     */
    public static boolean isFreecamEnabled() {
        if (mc.player == null) {
            return false;
        }
        return VoicechatClient.CLIENT_CONFIG.freecamSupport.get() && !(mc.player.isSpectator() || mc.player.equals(mc.getRenderViewEntity()));
    }

    /**
     * Gets the proximity reference point. Unless freecam is active, this is the main camera's position.
     *
     * @return the position distances should be measured from
     */
    public static Vec3d getReferencePoint() {
        if (mc.player == null) {
            return Vec3d.ZERO;
        }
        return isFreecamEnabled() ? mc.player.getPositionEyes(1F) : PositionalAudioUtils.getCameraPosition();
    }

    /**
     * Measures the distance to the provided position.
     * <p>
     * Distance is relative to either the player or camera, depending on whether freecam is enabled.
     *
     * @param pos the position to be measured
     * @return the distance to the position
     */
    public static double getDistanceTo(Vec3d pos) {
        return getReferencePoint().distanceTo(pos);
    }

    /**
     * Gets the volume for the provided distance.
     * <p>
     * Distance is relative to either the player or camera, depending on whether freecam is enabled.
     *
     * @param maxDistance the maximum distance of the sound
     * @param pos         the position of the audio
     * @return the resulting audio volume
     */
    public static float getDistanceVolume(float maxDistance, Vec3d pos) {
        return PositionalAudioUtils.getDistanceVolume(maxDistance, getReferencePoint(), pos);
    }
}
