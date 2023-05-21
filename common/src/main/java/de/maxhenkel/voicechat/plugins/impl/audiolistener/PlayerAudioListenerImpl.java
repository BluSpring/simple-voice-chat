package de.maxhenkel.voicechat.plugins.impl.audiolistener;

import de.maxhenkel.voicechat.api.ServerPlayer;
import de.maxhenkel.voicechat.api.audiolistener.PlayerAudioListener;
import de.maxhenkel.voicechat.api.packets.SoundPacket;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerAudioListenerImpl implements PlayerAudioListener {

    private final UUID playerUuid;
    private final Consumer<SoundPacket> listener;
    private final UUID listenerId;

    public PlayerAudioListenerImpl(UUID playerUuid, Consumer<SoundPacket> listener) {
        this.playerUuid = playerUuid;
        this.listener = listener;
        this.listenerId = UUID.randomUUID();
    }

    @Override
    public UUID getListenerId() {
        return listenerId;
    }

    @Override
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Consumer<SoundPacket> getListener() {
        return listener;
    }

    public static class BuilderImpl implements Builder {

        @Nullable
        private UUID playerUuid;
        @Nullable
        private Consumer<SoundPacket> listener;

        public BuilderImpl() {

        }

        @Override
        public Builder setPlayer(ServerPlayer player) {
            this.playerUuid = player.getUuid();
            return this;
        }

        @Override
        public Builder setPlayer(UUID playerUuid) {
            this.playerUuid = playerUuid;
            return this;
        }

        @Override
        public Builder setPacketListener(Consumer<SoundPacket> listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public PlayerAudioListener build() {
            if (playerUuid == null) {
                throw new IllegalStateException("No player provided");
            }
            if (listener == null) {
                throw new IllegalStateException("No listener provided");
            }
            return new PlayerAudioListenerImpl(playerUuid, listener);
        }
    }

}
