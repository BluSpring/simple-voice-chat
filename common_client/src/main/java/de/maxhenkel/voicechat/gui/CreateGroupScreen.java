package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.FontRendererExtension;
import de.maxhenkel.voicechat.extensions.GuiButtonExtension;
import de.maxhenkel.voicechat.gui.widgets.ButtonBase;
import de.maxhenkel.voicechat.gui.widgets.GuiTextField;
import de.maxhenkel.voicechat.net.ClientNetManager;
import de.maxhenkel.voicechat.net.CreateGroupPacket;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.src.StringTranslate;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class CreateGroupScreen extends VoiceChatScreenBase {

    private static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_create_group.png");
    private static final String TITLE = StringTranslate.getInstance().translateKey("gui.voicechat.create_group.title");
    private static final String CREATE = StringTranslate.getInstance().translateKey("message.voicechat.create");
    private static final String CREATE_GROUP = StringTranslate.getInstance().translateKey("message.voicechat.create_group");
    private static final String GROUP_NAME = StringTranslate.getInstance().translateKey("message.voicechat.group_name");
    private static final String OPTIONAL_PASSWORD = StringTranslate.getInstance().translateKey("message.voicechat.optional_password");
    private static final String GROUP_TYPE = StringTranslate.getInstance().translateKey("message.voicechat.group_type");

    private GuiTextField groupName;
    private GuiTextField password;
    private GroupType groupType;
    private ButtonBase groupTypeButton;
    private ButtonBase createGroup;

    public CreateGroupScreen() {
        super(TITLE, 195, 124);
        groupType = GroupType.NORMAL;
    }

    @Override
    public void initGui() {
        super.initGui();
        hoverAreas.clear();
        controlList.clear();

        Keyboard.enableRepeatEvents(true);

        groupName = new GuiTextField(0, fontRenderer, guiLeft + 7, guiTop + 32, xSize - 7 * 2, 10);
        groupName.setMaxStringLength(24);
        groupName.setValidator(s -> s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches());

        password = new GuiTextField(1, fontRenderer, guiLeft + 7, guiTop + 58, xSize - 7 * 2, 10);
        password.setMaxStringLength(32);
        password.setValidator(s -> s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches());

        groupTypeButton = new ButtonBase(2, guiLeft + 6, guiTop + 71, xSize - 12, 20, GROUP_TYPE + ": " + groupType.getTranslation()) {
            @Override
            public void onPress() {
                groupType = GroupType.values()[(groupType.ordinal() + 1) % GroupType.values().length];
                displayString = GROUP_TYPE + ": " + groupType.getTranslation();
            }
        };
        controlList.add(groupTypeButton);

        createGroup = new ButtonBase(3, guiLeft + 6, guiTop + ySize - 27, xSize - 12, 20, CREATE) {
            @Override
            public void onPress() {
                createGroup();
            }
        };
        controlList.add(createGroup);
    }

    private void createGroup() {
        if (!groupName.getText().isEmpty()) {
            ClientNetManager.sendToServer(new CreateGroupPacket(groupName.getText(), password.getText().isEmpty() ? null : password.getText(), groupType.getType()));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (groupName == null) {
            return;
        }
        groupName.updateCursorCounter();
        password.updateCursorCounter();
        createGroup.enabled = !groupName.getText().isEmpty();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void renderBackground(int mouseX, int mouseY, float delta) {
        TextureHelper.bindTexture(TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void renderForeground(int mouseX, int mouseY, float delta) {
        if (groupName == null) {
            return;
        }
        groupName.drawTextBox();
        password.drawTextBox();
        fontRenderer.drawString(CREATE_GROUP, guiLeft + xSize / 2 - fontRenderer.getStringWidth(CREATE_GROUP) / 2, guiTop + 7, FONT_COLOR);
        fontRenderer.drawString(GROUP_NAME, guiLeft + 8, guiTop + 7 + (int)TextureHelper.FONT_HEIGHT + 5, FONT_COLOR);
        fontRenderer.drawString(OPTIONAL_PASSWORD, guiLeft + 8, guiTop + 7 + ((int)TextureHelper.FONT_HEIGHT + 5) * 2 + 10 + 2, FONT_COLOR);

        if (mouseX >= groupTypeButton.xPosition && mouseY >= groupTypeButton.yPosition && mouseX < groupTypeButton.xPosition + ((GuiButtonExtension) groupTypeButton).getWidth() && mouseY < groupTypeButton.yPosition + ((GuiButtonExtension) groupTypeButton).getHeight()) {
            drawHoveringText(((FontRendererExtension) mc.fontRenderer).listFormattedStringToWidth(groupType.getDescription(), 200), mouseX, mouseY);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (groupName == null) {
            return;
        }
        if (groupName.textboxKeyTyped(typedChar, keyCode) | password.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }

        if (keyCode == Keyboard.KEY_RETURN) {
            createGroup();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (groupName == null) {
            return;
        }
        groupName.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void setWorldAndResolution(Minecraft minecraft, int width, int height) {
        String groupNameText = groupName.getText();
        String passwordText = password.getText();
        super.setWorldAndResolution(minecraft, width, height);
        groupName.setText(groupNameText);
        password.setText(passwordText);
    }

}
