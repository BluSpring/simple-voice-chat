package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.extensions.GuiExtension;
import de.maxhenkel.voicechat.gui.GameProfileUtils;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class GroupChatManager {

    private static final String TALK_OUTLINE = TextureHelper.format(Voicechat.MODID, "textures/icons/talk_outline.png");
    private static final String SPEAKER_OFF_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/speaker_small_off.png");

    public static void renderIcons() {
        ClientVoicechat client = ClientManager.getClient();

        if (client == null) {
            return;
        }
        Minecraft mc = MinecraftAccessor.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc.displayWidth, mc.displayHeight);

        List<PlayerState> groupMembers = getGroupMembers(VoicechatClient.CLIENT_CONFIG.showOwnGroupIcon.get());

        GL11.glPushMatrix();
        int posX = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosX.get();
        int posY = VoicechatClient.CLIENT_CONFIG.groupPlayerIconPosY.get();
        if (posX < 0) {
            GL11.glTranslated(scaledResolution.getScaledWidth(), 0D, 0D);
        }
        if (posY < 0) {
            GL11.glTranslated(0D, scaledResolution.getScaledHeight(), 0D);
        }
        GL11.glTranslated(posX, posY, 0D);

        float scale = VoicechatClient.CLIENT_CONFIG.groupHudIconScale.get().floatValue();
        GL11.glScalef(scale, scale, 1F);

        boolean vertical = VoicechatClient.CLIENT_CONFIG.groupPlayerIconOrientation.get().equals(GroupPlayerIconOrientation.VERTICAL);

        for (int i = 0; i < groupMembers.size(); i++) {
            PlayerState state = groupMembers.get(i);
            GL11.glPushMatrix();
            if (vertical) {
                if (posY < 0) {
                    GL11.glTranslated(0D, i * -11D, 0D);
                } else {
                    GL11.glTranslated(0D, i * 11D, 0D);
                }
            } else {
                if (posX < 0) {
                    GL11.glTranslated(i * -11D, 0D, 0D);
                } else {
                    GL11.glTranslated(i * 11D, 0D, 0D);
                }
            }

            if (client.getTalkCache().isTalking(state.getUuid())) {
                TextureHelper.bindTexture(TALK_OUTLINE);
                GuiExtension.staticDrawModalRectWithCustomSizedTexture(posX < 0 ? -10 : 0, posY < 0 ? -10 : 0, 0, 0, 10, 10, 16, 16);
            }
            GL11.glEnable(GL11.GL_BLEND);
            GameProfileUtils.bindSkinTexture(state.getName());
            GuiExtension.staticDrawModalRectWithCustomSizedTexture(posX < 0 ? -1 - 8 : 1, posY < 0 ? -1 - 8 : 1, 8, 8, 8, 8, 64, 64);
            GuiExtension.staticDrawModalRectWithCustomSizedTexture(posX < 0 ? -1 - 8 : 1, posY < 0 ? -1 - 8 : 1, 40, 8, 8, 8, 64, 64);

            if (state.isDisabled()) {
                GL11.glPushMatrix();
                GL11.glTranslated((posX < 0 ? -1D - 8D : 1D), posY < 0 ? -1D - 8D : 1D, 0D);
                GL11.glScalef(0.5F, 0.5F, 1F);
                TextureHelper.bindTexture(SPEAKER_OFF_ICON);
                GuiExtension.staticDrawModalRectWithCustomSizedTexture(0, 0, 0, 0, 16, 16, 16, 16);
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }

    public static List<PlayerState> getGroupMembers() {
        return getGroupMembers(true);
    }

    public static List<PlayerState> getGroupMembers(boolean includeSelf) {
        List<PlayerState> entries = new ArrayList<>();
        UUID group = ClientManager.getPlayerStateManager().getGroupID();

        if (group == null) {
            return entries;
        }

        for (PlayerState state : ClientManager.getPlayerStateManager().getPlayerStates(includeSelf)) {
            if (state.hasGroup() && state.getGroup().equals(group)) {
                entries.add(state);
            }
        }

        entries.sort(Comparator.comparing(PlayerState::getName));

        return entries;
    }

}
