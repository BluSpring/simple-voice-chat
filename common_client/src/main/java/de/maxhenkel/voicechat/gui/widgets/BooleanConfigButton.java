package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.configbuilder.ConfigEntry;

import java.util.function.Function;

public class BooleanConfigButton extends ButtonBase {

    private ConfigEntry<Boolean> entry;
    private Function<Boolean, String> component;

    public BooleanConfigButton(int id, int x, int y, int width, int height, ConfigEntry<Boolean> entry, Function<Boolean, String> component) {
        super(id, x, y, width, height, "");
        this.entry = entry;
        this.component = component;
        updateText();
    }

    private void updateText() {
        displayString = component.apply(entry.get());
    }

    @Override
    public void onPress() {
        entry.set(!entry.get()).save();
        updateText();
    }

}
