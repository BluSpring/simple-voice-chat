package de.maxhenkel.voicechat.gui.group;

import com.google.common.collect.Lists;
import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.GuiExtension;
import de.maxhenkel.voicechat.gui.GameProfileUtils;
import de.maxhenkel.voicechat.gui.VoiceChatScreenBase;
import de.maxhenkel.voicechat.gui.GroupType;
import de.maxhenkel.voicechat.gui.widgets.ListScreenBase;
import de.maxhenkel.voicechat.gui.widgets.ListScreenEntryBase;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class JoinGroupEntry extends ListScreenEntryBase {

    protected static final String LOCK = TextureHelper.format(Voicechat.MODID, "textures/icons/lock.png");
    protected static final String GROUP_MEMBERS = StringTranslate.getInstance().translateKey("message.voicechat.group_members");
    protected static final String NO_GROUP_MEMBERS = StringTranslate.getInstance().translateKey("message.voicechat.no_group_members");

    protected static final int SKIN_SIZE = 12;
    protected static final int PADDING = 4;
    protected static final int BG_FILL = VoiceChatScreenBase.color(255, 74, 74, 74);
    protected static final int BG_FILL_SELECTED = VoiceChatScreenBase.color(255, 90, 90, 90);
    protected static final int PLAYER_NAME_COLOR = VoiceChatScreenBase.color(255, 255, 255, 255);

    protected final ListScreenBase parent;
    protected final Minecraft minecraft;
    protected final Group group;

    public JoinGroupEntry(ListScreenBase parent, Group group) {
        this.parent = parent;
        this.minecraft = MinecraftAccessor.getMinecraft();
        this.group = group;
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, partialTicks);
        if (isSelected) {
            parent.drawRect(x, y, x + listWidth, y + slotHeight, BG_FILL_SELECTED);
        } else {
            parent.drawRect(x, y, x + listWidth, y + slotHeight, BG_FILL);
        }

        boolean hasPassword = group.group.hasPassword();

        if (hasPassword) {
            GL11.glColor4f(1F, 1F, 1F, 1F);
            TextureHelper.bindTexture(LOCK);
            ((GuiExtension) parent).drawModalRectWithCustomSizedTexture(x + PADDING, y + slotHeight / 2 - 8, 0, 0, 16, 16, 16, 16);
        }

        minecraft.fontRenderer.drawString(group.group.getName(), x + PADDING + (hasPassword ? 16 + PADDING : 0), (int) (y + slotHeight / 2 - TextureHelper.FONT_HEIGHT / 2), PLAYER_NAME_COLOR);

        int textWidth = minecraft.fontRenderer.getStringWidth(group.group.getName()) + (hasPassword ? 16 + PADDING : 0);

        int headsPerRow = (listWidth - (PADDING + textWidth + PADDING + PADDING)) / (SKIN_SIZE + 1);
        int rows = 2;

        for (int i = 0; i < group.members.size(); i++) {
            PlayerState state = group.members.get(i);

            int headXIndex = i / rows;
            int headYIndex = i % rows;

            if (i >= headsPerRow * rows) {
                break;
            }

            int headPosX = x + listWidth - SKIN_SIZE - PADDING - headXIndex * (SKIN_SIZE + 1);
            int headPosY = y + slotHeight / 2 - ((SKIN_SIZE * 2 + 2) / 2) + ((SKIN_SIZE * 2 + 2) / 2) * headYIndex;

            GL11.glPushMatrix();
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GameProfileUtils.bindSkinTexture(state.getName());
            GL11.glTranslatef(headPosX, headPosY, 0);
            float scale = (float) SKIN_SIZE / 8F;
            GL11.glScalef(scale, scale, scale);
            ((GuiExtension) parent).drawModalRectWithCustomSizedTexture(0, 0, 8, 8, 8, 8, 64, 64);
            GL11.glEnable(GL11.GL_BLEND);
            ((GuiExtension) parent).drawModalRectWithCustomSizedTexture(0, 0, 40, 8, 8, 8, 64, 64);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }

        if (!isSelected) {
            return;
        }
        List<String> tooltip = Lists.newArrayList();

        if (group.getGroup().getType().equals(de.maxhenkel.voicechat.api.Group.Type.NORMAL)) {
            tooltip.add(String.format(StringTranslate.getInstance().translateKey("message.voicechat.group_title"), group.getGroup().getName()));
        } else {
            tooltip.add(String.format(StringTranslate.getInstance().translateKey("message.voicechat.group_type_title"), group.getGroup().getName(), GroupType.fromType(group.getGroup().getType()).getTranslation()));
        }
        if (group.getMembers().isEmpty()) {
            tooltip.add(NO_GROUP_MEMBERS);
        } else {
            tooltip.add(GROUP_MEMBERS);
            int maxMembers = 10;
            for (int i = 0; i < group.getMembers().size(); i++) {
                if (i >= maxMembers) {
                    tooltip.add(String.format(StringTranslate.getInstance().translateKey("message.voicechat.more_members"), group.getMembers().size() - maxMembers));
                    break;
                }
                PlayerState state = group.getMembers().get(i);
                tooltip.add("  " + state.getName());
            }
        }

        parent.postRender(() -> {
            for (int i = 0; i < tooltip.size(); i++) {
                String s = tooltip.get(i);
                mc.fontRenderer.drawStringWithShadow(s, mouseX, (int) (i * TextureHelper.FONT_HEIGHT + 2) + mouseY, 16777215);
            }
        });
    }

    public Group getGroup() {
        return group;
    }

    public static class Group {
        private final ClientGroup group;
        private final List<PlayerState> members;

        public Group(ClientGroup group) {
            this.group = group;
            this.members = new ArrayList<>();
        }

        public ClientGroup getGroup() {
            return group;
        }

        public List<PlayerState> getMembers() {
            return members;
        }
    }

}
