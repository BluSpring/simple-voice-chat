package de.maxhenkel.voicechat.voice.client;

public interface SoundManagerApi {
    int SAMPLE_RATE = 48000;
    int FRAME_SIZE = (SAMPLE_RATE / 1000) * 20;
    int MAX_PAYLOAD_SIZE = 1024;
}
