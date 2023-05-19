package de.maxhenkel.voicechat.gui.volume;

import de.maxhenkel.voicechat.gui.widgets.DebouncedSlider;
import net.minecraft.src.StringTranslate;

public class AdjustVolumeSlider extends DebouncedSlider {

    protected static final String MUTED = StringTranslate.getInstance().translateKey("message.voicechat.muted");

    protected static final float MAXIMUM = 4F;

    protected final VolumeConfigEntry volumeConfigEntry;

    public AdjustVolumeSlider(int id, int xIn, int yIn, int widthIn, int heightIn, VolumeConfigEntry volumeConfigEntry) {
        super(id, xIn, yIn, widthIn, heightIn, volumeConfigEntry.get() / MAXIMUM);
        this.volumeConfigEntry = volumeConfigEntry;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        if (value <= 0D) {
            displayString = MUTED;
            return;
        }
        long amp = Math.round(value * MAXIMUM * 100F - 100F);
        displayString = String.format(StringTranslate.getInstance().translateKey("message.voicechat.volume_amplification"), (amp > 0F ? "+" : "") + amp + "%");
    }

    @Override
    public void applyDebounced() {
        volumeConfigEntry.save(value * MAXIMUM);
    }

    public interface VolumeConfigEntry {
        void save(double value);

        double get();
    }

}
