package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import de.maxhenkel.voicechat.config.ServerConfig;
import de.maxhenkel.voicechat.extensions.EntityPlayerExtension;
import de.maxhenkel.voicechat.plugins.PluginManager;
import de.maxhenkel.voicechat.plugins.impl.opus.OpusManager;
import de.maxhenkel.voicechat.voice.client.microphone.Microphone;
import de.maxhenkel.voicechat.voice.client.microphone.MicrophoneManager;
import de.maxhenkel.voicechat.voice.common.MicPacket;
import de.maxhenkel.voicechat.voice.common.NetworkMessage;
import de.maxhenkel.voicechat.voice.common.Utils;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class MicThread extends Thread {

    @Nullable
    private final ClientVoicechat client;
    @Nullable
    private final ClientVoicechatConnection connection;
    private final Microphone mic;
    private final VolumeManager volumeManager;
    private boolean running;
    private boolean microphoneLocked;
    private boolean wasWhispering;
    private final OpusEncoder encoder;
    @Nullable
    private Denoiser denoiser;

    public MicThread(@Nullable ClientVoicechat client, @Nullable ClientVoicechatConnection connection) throws MicrophoneException, NativeDependencyException {
        this.client = client;
        this.connection = connection;
        this.running = true;
        this.encoder = OpusManager.createEncoder(connection == null ? ServerConfig.Codec.VOIP.getMode() : connection.getData().getCodec().getMode());

        this.denoiser = Denoiser.createDenoiser();
        if (denoiser == null) {
            Voicechat.LOGGER.warn("Denoiser not available");
        }
        volumeManager = new VolumeManager();

        setDaemon(true);
        setName("MicrophoneThread");

        mic = MicrophoneManager.createMicrophone();
    }

    @Override
    public void run() {
        while (running) {
            if (connection != null) {
                // Checking here for timeouts, because we don't have any other looping thread
                connection.checkTimeout();
                if (!running) {
                    break;
                }
            }
            if (microphoneLocked || ClientManager.getPlayerStateManager().isDisabled()) {
                activating = false;
                wasPTT = false;
                wasWhispering = false;
                flush();

                if (!microphoneLocked && ClientManager.getPlayerStateManager().isDisabled()) {
                    if (mic.isStarted()) {
                        mic.stop();
                    }
                    if (denoiser != null) {
                        denoiser.close();
                    }
                }

                Utils.sleep(10);
                continue;
            }

            short[] audio = pollMic();
            if (audio == null) {
                continue;
            }
            MicrophoneActivationType type = VoicechatClient.CLIENT_CONFIG.microphoneActivationType.get();
            if (type.equals(MicrophoneActivationType.PTT)) {
                ptt(audio);
            } else if (type.equals(MicrophoneActivationType.VOICE)) {
                voice(audio);
            }
        }
    }

    @Nullable
    public short[] pollMic() {
        if (!mic.isStarted()) {
            mic.start();
        }
        if (denoiser != null && denoiser.isClosed()) {
            denoiser = Denoiser.createDenoiser();
        }

        if (mic.available() < SoundManager.FRAME_SIZE) {
            Utils.sleep(5);
            return null;
        }
        short[] buff = mic.read();
        volumeManager.adjustVolumeMono(buff, VoicechatClient.CLIENT_CONFIG.microphoneAmplification.get().floatValue());
        return denoiseIfEnabled(buff);
    }

    private volatile boolean activating;
    private volatile int deactivationDelay;
    private volatile short[] lastBuff;

    private void voice(short[] audio) {
        wasPTT = false;

        if (ClientManager.getPlayerStateManager().isMuted()) {
            activating = false;
            wasWhispering = false;
            flush();
            return;
        }

        wasWhispering = ClientManager.getPttKeyHandler().isWhisperDown();

        int offset = Utils.getActivationOffset(audio, VoicechatClient.CLIENT_CONFIG.voiceActivationThreshold.get());
        if (activating) {
            if (offset < 0) {
                if (deactivationDelay >= VoicechatClient.CLIENT_CONFIG.deactivationDelay.get()) {
                    activating = false;
                    deactivationDelay = 0;
                    flush();
                } else {
                    sendAudioPacket(audio, wasWhispering);
                    deactivationDelay++;
                }
            } else {
                sendAudioPacket(audio, wasWhispering);
            }
        } else {
            if (offset > 0) {
                if (lastBuff != null) {
                    sendAudioPacket(lastBuff, wasWhispering);
                }
                sendAudioPacket(audio, wasWhispering);
                activating = true;
            }
        }
        lastBuff = audio;
    }

    private volatile boolean wasPTT;

    private void ptt(short[] audio) {
        activating = false;
        if (!ClientManager.getPttKeyHandler().isAnyDown()) {
            if (wasPTT) {
                wasPTT = false;
                wasWhispering = false;
                flush();
            }
            return;
        }
        wasPTT = true;
        wasWhispering = ClientManager.getPttKeyHandler().isWhisperDown();
        sendAudioPacket(audio, wasWhispering);
    }

    public short[] denoiseIfEnabled(short[] audio) {
        if (denoiser != null && VoicechatClient.CLIENT_CONFIG.denoiser.get()) {
            return denoiser.denoise(audio);
        }
        return audio;
    }

    private void flush() {
        sendStopPacket();
        if (!encoder.isClosed()) {
            encoder.resetState();
        }
        if (client == null) {
            return;
        }
        AudioRecorder recorder = client.getRecorder();
        if (recorder == null) {
            return;
        }
        recorder.flushChunkThreaded(((EntityPlayerExtension) MinecraftAccessor.getMinecraft().thePlayer).getUniqueID());
    }

    public boolean isTalking() {
        return !microphoneLocked && (activating || wasPTT);
    }

    public boolean isWhispering() {
        return isTalking() && wasWhispering;
    }

    public void setMicrophoneLocked(boolean microphoneLocked) {
        this.microphoneLocked = microphoneLocked;
        activating = false;
        wasPTT = false;
        deactivationDelay = 0;
        lastBuff = null;
    }

    public void close() {
        if (!running) {
            return;
        }
        running = false;

        if (Thread.currentThread() != this) {
            try {
                join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mic.close();
        encoder.close();
        if (denoiser != null) {
            denoiser.close();
        }
        flush();
    }

    private final AtomicLong sequenceNumber = new AtomicLong();
    private volatile boolean stopPacketSent = true;

    private void sendAudioPacket(short[] data, boolean whispering) {
        short[] audio = PluginManager.instance().onClientSound(data, whispering);
        if (audio == null) {
            return;
        }

        try {
            if (connection != null && connection.isInitialized()) {
                byte[] encoded = encoder.encode(audio);
                connection.sendToServer(new NetworkMessage(new MicPacket(encoded, whispering, sequenceNumber.getAndIncrement())));
                stopPacketSent = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (client != null && client.getRecorder() != null) {
                client.getRecorder().appendChunk(((EntityPlayerExtension) MinecraftAccessor.getMinecraft().thePlayer).getUniqueID(), System.currentTimeMillis(), PositionalAudioUtils.convertToStereo(audio));
            }
        } catch (IOException e) {
            Voicechat.LOGGER.error("Failed to record audio", e);
            client.setRecording(false);
        }
    }

    private void sendStopPacket() {
        if (stopPacketSent) {
            return;
        }

        if (connection == null || !connection.isInitialized()) {
            return;
        }
        try {
            connection.sendToServer(new NetworkMessage(new MicPacket(new byte[0], false, sequenceNumber.getAndIncrement())));
            stopPacketSent = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
