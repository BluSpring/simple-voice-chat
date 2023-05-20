package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.extensions.GuiExtension;
import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.util.UUID;

public class RenderEvents {

    private static final String MICROPHONE_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/microphone.png");
    private static final String WHISPER_MICROPHONE_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/microphone_whisper.png");
    private static final String MICROPHONE_OFF_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/microphone_off.png");
    private static final String SPEAKER_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/speaker.png");
    private static final String WHISPER_SPEAKER_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/speaker_whisper.png");
    private static final String SPEAKER_OFF_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/speaker_off.png");
    private static final String DISCONNECT_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/disconnected.png");
    private static final String GROUP_ICON = TextureHelper.format(Voicechat.MODID, "textures/icons/group.png");

    private final Minecraft minecraft;

    public RenderEvents() {
        minecraft = MinecraftAccessor.getMinecraft();
        ClientCompatibilityManager.INSTANCE.onRenderNamePlate(this::onRenderName);
        ClientCompatibilityManager.INSTANCE.onRenderHUD(this::onRenderHUD);
    }

    private void onRenderHUD(float tickDelta) {
        if (!shouldShowIcons()) {
            return;
        }
        if (VoicechatClient.CLIENT_CONFIG.hideIcons.get()) {
            return;
        }

        ClientPlayerStateManager manager = ClientManager.getPlayerStateManager();
        ClientVoicechat client = ClientManager.getClient();

        if (manager.isDisconnected() && isStartup()) {
            return;
        }

        if (manager.isDisconnected()) {
            renderIcon(DISCONNECT_ICON);
        } else if (manager.isDisabled()) {
            renderIcon(SPEAKER_OFF_ICON);
        } else if (manager.isMuted() && VoicechatClient.CLIENT_CONFIG.microphoneActivationType.get().equals(MicrophoneActivationType.VOICE)) {
            renderIcon(MICROPHONE_OFF_ICON);
        } else if (client != null && client.getMicThread() != null) {
            if (client.getMicThread().isWhispering()) {
                renderIcon(WHISPER_MICROPHONE_ICON);
            } else if (client.getMicThread().isTalking()) {
                renderIcon(MICROPHONE_ICON);
            }
        }

        if (manager.getGroupID() != null && VoicechatClient.CLIENT_CONFIG.showGroupHUD.get()) {
            GroupChatManager.renderIcons();
        }
    }

    private boolean isStartup() {
        ClientVoicechat client = ClientManager.getClient();
        return client != null && (System.currentTimeMillis() - client.getStartTime()) < 5000;
    }

    private void renderIcon(String texture) {
        GL11.glPushMatrix();
        ScaledResolution scaledResolution = new ScaledResolution(minecraft.displayWidth, minecraft.displayHeight);
        TextureHelper.bindTexture(texture);
        int posX = VoicechatClient.CLIENT_CONFIG.hudIconPosX.get();
        int posY = VoicechatClient.CLIENT_CONFIG.hudIconPosY.get();
        if (posX < 0) {
            GL11.glTranslated(scaledResolution.getScaledWidth(), 0D, 0D);
        }
        if (posY < 0) {
            GL11.glTranslated(0D, scaledResolution.getScaledHeight(), 0D);
        }
        GL11.glTranslated(posX, posY, 0D);
        float scale = VoicechatClient.CLIENT_CONFIG.hudIconScale.get().floatValue();
        GL11.glScalef(scale, scale, 1F);

        GuiExtension.staticDrawModalRectWithCustomSizedTexture(posX < 0 ? -16 : 0, posY < 0 ? -16 : 0, 0, 0, 16, 16, 16, 16);
        GL11.glPopMatrix();
    }

    private void onRenderName(Entity entity, String str, double x, double y, double z, int maxDistance) {
        if (!shouldShowIcons()) {
            return;
        }
        if (VoicechatClient.CLIENT_CONFIG.hideIcons.get()) {
            return;
        }
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) entity;
        if (entity == minecraft.thePlayer) {
            return;
        }

        if (false) {
            ClientPlayerStateManager manager = ClientManager.getPlayerStateManager();
            ClientVoicechat client = ClientManager.getClient();
            UUID groupId = manager.getGroup(player);

            if (client != null && client.getTalkCache().isWhispering(player)) {
                renderPlayerIcon(player, str, x, y, z, maxDistance, WHISPER_SPEAKER_ICON);
            } else if (client != null && client.getTalkCache().isTalking(player)) {
                renderPlayerIcon(player, str, x, y, z, maxDistance, SPEAKER_ICON);
            } else if (manager.isPlayerDisconnected(player)) {
                renderPlayerIcon(player, str, x, y, z, maxDistance, DISCONNECT_ICON);
            } else if (groupId != null && !groupId.equals(manager.getGroupID())) {
                renderPlayerIcon(player, str, x, y, z, maxDistance, GROUP_ICON);
            } else if (manager.isPlayerDisabled(player)) {
                renderPlayerIcon(player, str, x, y, z, maxDistance, SPEAKER_OFF_ICON);
            }
        }
    }

    private void renderPlayerIcon(EntityPlayer entity, String str, double x, double y, double z, int maxDistance, String texture) {
        boolean isThirdPersonFrontal = false;
        int verticalShift = "deadmau5".equals(str) ? -10 : 0;

        float height = entity.height + 0.5F - (entity.isSneaking() ? 0.25F : 0F);

        GL11.glPushMatrix();
        GL11.glTranslated(x, y + height, z);
        GL11.glNormal3f(0F, 1F, 0F);
        GL11.glRotatef(-minecraft.thePlayer.rotationYaw, 0F, 1F, 0F);
        GL11.glRotatef((float) (isThirdPersonFrontal ? -1 : 1) * minecraft.thePlayer.rotationPitch, 1F, 0F, 0F);
        GL11.glScalef(-0.025F, -0.025F, 0.025F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);

        if (!entity.isSneaking()) {
            GL11.glDisable(GL11.GL_DEPTH);
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL14.glBlendFuncSeparate(GL14.GL_BLEND_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        int halfNameWidth = minecraft.fontRenderer.getStringWidth(str) / 2;
        GL11.glTranslatef(halfNameWidth, verticalShift - 1F, 0F);
        if (!entity.isSneaking()) {
            drawIcon(texture, true);
            GL11.glEnable(GL11.GL_DEPTH);
        }

        GL11.glDepthMask(true);
        drawIcon(texture, false);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glPopMatrix();
    }

    private void drawIcon(String texture, boolean transparent) {
        TextureHelper.bindTexture(texture);
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(7);
        tessellator.setColorRGBA(255, 255, 255, transparent ? 32 : 255);
        tessellator.addVertexWithUV(2D, 10D, 0D, 0D, 1D);
        tessellator.setColorRGBA(255, 255, 255, transparent ? 32 : 255);
        tessellator.addVertexWithUV(2D + 10D, 10D, 0D, 1D, 1D);
        tessellator.setColorRGBA(255, 255, 255, transparent ? 32 : 255);
        tessellator.addVertexWithUV(2D + 10D, 0D, 0D, 1D, 0D);
        tessellator.setColorRGBA(255, 255, 255, transparent ? 32 : 255);
        tessellator.addVertexWithUV(2D, 0D, 0D, 0D, 0D);
        tessellator.draw();
    }

    private boolean shouldShowIcons() {
        if (ClientManager.getClient() != null && ClientManager.getClient().getConnection() != null && ClientManager.getClient().getConnection().isInitialized()) {
            return true;
        }
        return minecraft.isMultiplayerWorld();
    }

}
