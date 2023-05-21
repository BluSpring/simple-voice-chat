package de.maxhenkel.voicechat.plugins.impl.opus;

import de.maxhenkel.voicechat.Voicechat;
import org.concentus.OpusDecoder;
import org.concentus.OpusException;

import javax.annotation.Nullable;

public class JavaOpusDecoderImpl implements de.maxhenkel.voicechat.api.opus.OpusDecoder {

    protected OpusDecoder opusDecoder;
    protected short[] buffer;
    protected int sampleRate;

    public JavaOpusDecoderImpl(int sampleRate, int frameSize) {
        this.sampleRate = sampleRate;
        this.buffer = new short[frameSize];
        open();
    }

    private void open() {
        if (opusDecoder != null) {
            return;
        }
        try {
            opusDecoder = new OpusDecoder(sampleRate, 1);
        } catch (OpusException e) {
            throw new IllegalStateException("Failed to create Opus decoder", e);
        }
        Voicechat.logDebug("Initializing Java Opus decoder with sample rate {} Hz, frame size {} bytes", sampleRate, buffer.length);
    }

    @Override
    public short[] decode(@Nullable byte[] data) {
        if (isClosed()) {
            throw new IllegalStateException("Decoder is closed");
        }
        int result;

        try {
            if (data == null || data.length == 0) {
                result = opusDecoder.decode(null, 0, 0, buffer, 0, buffer.length, true);
            } else {
                result = opusDecoder.decode(data, 0, data.length, buffer, 0, buffer.length, false);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode audio", e);
        }

        short[] audio = new short[result];
        System.arraycopy(buffer, 0, audio, 0, result);
        return audio;
    }

    @Override
    public boolean isClosed() {
        return opusDecoder == null;
    }

    @Override
    public void close() {
        if (isClosed()) {
            return;
        }
        opusDecoder = null;
    }

    @Override
    public void resetState() {
        if (isClosed()) {
            throw new IllegalStateException("Decoder is closed");
        }
        opusDecoder.resetState();
    }

}
