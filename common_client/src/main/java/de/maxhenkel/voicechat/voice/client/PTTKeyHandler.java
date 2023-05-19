package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class PTTKeyHandler {

    private boolean pttKeyDown;
    private boolean whisperKeyDown;

    public PTTKeyHandler() {
        ClientCompatibilityManager.INSTANCE.onKeyboardEvent(this::onKeyboardEvent);
        ClientCompatibilityManager.INSTANCE.onMouseEvent(this::onMouseEvent);
    }

    public void onKeyboardEvent() {
        if (KeyEvents.KEY_PTT.keyCode > 0 && KeyEvents.KEY_PTT.keyCode < 256) {
            pttKeyDown = Keyboard.isKeyDown(KeyEvents.KEY_PTT.keyCode);
        }
        if (KeyEvents.KEY_WHISPER.keyCode > 0 && KeyEvents.KEY_PTT.keyCode < 256) {
            whisperKeyDown = Keyboard.isKeyDown(KeyEvents.KEY_WHISPER.keyCode);
        }
    }

    public void onMouseEvent() {
        if (KeyEvents.KEY_PTT.keyCode < 0) {
            pttKeyDown = Mouse.isButtonDown(KeyEvents.KEY_PTT.keyCode + 100);
        }
        if (KeyEvents.KEY_WHISPER.keyCode < 0) {
            whisperKeyDown = Mouse.isButtonDown(KeyEvents.KEY_WHISPER.keyCode + 100);
        }
    }

    public boolean isPTTDown() {
        return pttKeyDown;
    }

    public boolean isWhisperDown() {
        return whisperKeyDown;
    }

    public boolean isAnyDown() {
        return pttKeyDown || whisperKeyDown;
    }

}
