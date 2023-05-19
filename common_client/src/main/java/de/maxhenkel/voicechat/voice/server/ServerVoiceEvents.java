package de.maxhenkel.voicechat.voice.server;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.extensions.EntityPlayerExtension;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.net.SecretPacket;
import de.maxhenkel.voicechat.plugins.PluginManager;
import net.minecraft.src.EntityPlayer;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerVoiceEvents {

    private final Map<UUID, Integer> clientCompatibilities;
    private Server server;

    public ServerVoiceEvents() {
        clientCompatibilities = new ConcurrentHashMap<>();
        CommonCompatibilityManager.INSTANCE.onServerStarting(this::serverStarting);
        CommonCompatibilityManager.INSTANCE.onPlayerLoggedIn(this::playerLoggedIn);
        CommonCompatibilityManager.INSTANCE.onPlayerLoggedOut(this::playerLoggedOut);
        CommonCompatibilityManager.INSTANCE.onServerStopping(this::serverStopping);

        CommonCompatibilityManager.INSTANCE.getNetManager().requestSecretChannel.setServerListener((player, handler, packet) -> {
            Voicechat.LOGGER.info("Received secret request of {} ({})", player.username, packet.getCompatibilityVersion());
            clientCompatibilities.put(((EntityPlayerExtension) player).getUniqueID(), packet.getCompatibilityVersion());
            if (packet.getCompatibilityVersion() != Voicechat.COMPATIBILITY_VERSION) {
                Voicechat.LOGGER.warn("Connected client {} has incompatible voice chat version (server={}, client={})", player.username, Voicechat.COMPATIBILITY_VERSION, packet.getCompatibilityVersion());
                //player.sendMessage(getIncompatibleMessage(packet.getCompatibilityVersion()));
            } else {
                initializePlayerConnection(player);
            }
        });
    }

    public boolean isCompatible(EntityPlayer player) {
        return isCompatible(((EntityPlayerExtension) player).getUniqueID());
    }

    public boolean isCompatible(UUID playerUuid) {
        return clientCompatibilities.getOrDefault(playerUuid, -1) == Voicechat.COMPATIBILITY_VERSION;
    }

    public void serverStarting(Object no) {
        if (server != null) {
            server.close();
            server = null;
        }

        if (VoicechatClient.CLIENT_CONFIG != null && !VoicechatClient.CLIENT_CONFIG.runLocalServer.get()) {
            Voicechat.LOGGER.info("Disabling voice chat in singleplayer");
            return;
        }

        /*if (mcServer.isDedicatedServer()) {
            if (!mcServer.isServerInOnlineMode()) {
                Voicechat.LOGGER.warn("Running in offline mode - Voice chat encryption is not secure!");
            }
        }*/

        try {
            server = new Server();
            server.start();
            PluginManager.instance().onServerStarted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializePlayerConnection(EntityPlayer player) {
        if (server == null) {
            return;
        }
        CommonCompatibilityManager.INSTANCE.emitPlayerCompatibilityCheckSucceeded(player);

        UUID secret = server.getSecret(((EntityPlayerExtension) player).getUniqueID());
        NetManager.sendToClient(player, new SecretPacket(player, secret, server.getPort(), Voicechat.SERVER_CONFIG));
        Voicechat.LOGGER.info("Sent secret to {}", player.username);
    }

    public void playerLoggedIn(EntityPlayer serverPlayer) {
        if (!Voicechat.SERVER_CONFIG.forceVoiceChat.get()) {
            return;
        }
    }

    public void playerLoggedOut(EntityPlayer player) {
        clientCompatibilities.remove(((EntityPlayerExtension) player).getUniqueID());
        if (server == null) {
            return;
        }

        server.disconnectClient(((EntityPlayerExtension) player).getUniqueID());
        Voicechat.LOGGER.info("Disconnecting client {}", player.username);
    }

    @Nullable
    public Server getServer() {
        return server;
    }

    public void serverStopping(Object no) {
        if (server != null) {
            server.close();
            server = null;
        }
    }

}
