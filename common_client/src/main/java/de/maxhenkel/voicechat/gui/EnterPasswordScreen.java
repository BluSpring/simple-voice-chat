package de.maxhenkel.voicechat.gui;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.gui.widgets.ButtonBase;
import de.maxhenkel.voicechat.gui.widgets.GuiTextField;
import de.maxhenkel.voicechat.net.ClientNetManager;
import de.maxhenkel.voicechat.net.JoinGroupPacket;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.util.TextureHelper;
import de.maxhenkel.voicechat.voice.common.ClientGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.src.StringTranslate;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class EnterPasswordScreen extends VoiceChatScreenBase {

    private static final String TEXTURE = TextureHelper.format(Voicechat.MODID, "textures/gui/gui_enter_password.png");
    private static final String TITLE = StringTranslate.getInstance().translateKey("gui.voicechat.enter_password.title");
    private static final String JOIN_GROUP = StringTranslate.getInstance().translateKey("message.voicechat.join_group");
    private static final String ENTER_GROUP_PASSWORD = StringTranslate.getInstance().translateKey("message.voicechat.enter_group_password");
    private static final String PASSWORD = StringTranslate.getInstance().translateKey("message.voicechat.password");

    private GuiTextField password;
    private ButtonBase joinGroup;
    private ClientGroup group;

    public EnterPasswordScreen(ClientGroup group) {
        super(TITLE, 195, 74);
        this.group = group;
    }

    @Override
    public void initGui() {
        super.initGui();
        hoverAreas.clear();
        controlList.clear();

        Keyboard.enableRepeatEvents(true);

        password = new GuiTextField(0, fontRenderer, guiLeft + 7, guiTop + 7 + ((int)TextureHelper.FONT_HEIGHT + 5) * 2 - 5 + 2, xSize - 7 * 2, 10);
        password.setMaxStringLength(32);
        password.setValidator(s -> s.isEmpty() || Voicechat.GROUP_REGEX.matcher(s).matches());

        joinGroup = new ButtonBase(1, guiLeft + 7, guiTop + ySize - 20 - 7, xSize - 7 * 2, 20, JOIN_GROUP) {
            @Override
            public void onPress() {
                joinGroup();
            }
        };
        controlList.add(joinGroup);
    }

    private void joinGroup() {
        if (!password.getText().isEmpty()) {
            ClientNetManager.sendToServer(new JoinGroupPacket(group.getId(), password.getText()));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (password == null) {
            return;
        }
        password.updateCursorCounter();
        joinGroup.enabled = !password.getText().isEmpty();
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
        if (password != null) {
            password.drawTextBox();
        }
        fontRenderer.drawString(ENTER_GROUP_PASSWORD, guiLeft + xSize / 2 - fontRenderer.getStringWidth(ENTER_GROUP_PASSWORD) / 2, guiTop + 7, FONT_COLOR);
        fontRenderer.drawString(PASSWORD, guiLeft + 8, guiTop + 7 + (int) TextureHelper.FONT_HEIGHT + 5, FONT_COLOR);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (password == null) {
            return;
        }
        if (password.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }

        if (keyCode == Keyboard.KEY_RETURN) {
            joinGroup();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (password == null) {
            return;
        }
        password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void setWorldAndResolution(Minecraft minecraft, int width, int height) {
        String passwordText = password.getText();
        super.setWorldAndResolution(minecraft, width, height);
        password.setText(passwordText);
    }
}
