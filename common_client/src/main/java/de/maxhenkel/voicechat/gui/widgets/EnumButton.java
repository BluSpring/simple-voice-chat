package de.maxhenkel.voicechat.gui.widgets;

import de.maxhenkel.configbuilder.ConfigEntry;

public abstract class EnumButton<T extends Enum<T>> extends ButtonBase {

    protected ConfigEntry<T> entry;

    public EnumButton(int id, int xIn, int yIn, int widthIn, int heightIn, ConfigEntry<T> entry) {
        super(id, xIn, yIn, widthIn, heightIn, "");
        this.entry = entry;
        updateText();
    }

    protected void updateText() {
        displayString = getText(entry.get());
    }

    protected abstract String getText(T type);

    protected void onUpdate(T type) {

    }

    @Override
    public void onPress() {
        T e = entry.get();
        Enum<T>[] values = e.getClass().getEnumConstants();
        T type = (T) values[(e.ordinal() + 1) % values.length];
        entry.set(type).save();
        updateText();
        onUpdate(type);
    }

}
