package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.extensions.NetClientHandlerExtension;
import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
import de.maxhenkel.voicechat.net.*;
import de.maxhenkel.voicechat.voice.server.Server;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.StringTranslate;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ClientManager {

    @Nullable
    private ClientVoicechat client;
    private final ClientPlayerStateManager playerStateManager;
    private final ClientGroupManager groupManager;
    private final ClientCategoryManager categoryManager;
    private final PTTKeyHandler pttKeyHandler;
    private final RenderEvents renderEvents;
    private final KeyEvents keyEvents;
    private final Minecraft minecraft;

    private ClientManager() {
        playerStateManager = new ClientPlayerStateManager();
        groupManager = new ClientGroupManager();
        categoryManager = new ClientCategoryManager();
        pttKeyHandler = new PTTKeyHandler();
        renderEvents = new RenderEvents();
        keyEvents = new KeyEvents();
        minecraft = MinecraftAccessor.getMinecraft();

        ClientCompatibilityManager.INSTANCE.onJoinWorld(this::onJoinWorld);
        ClientCompatibilityManager.INSTANCE.onDisconnect(this::onDisconnect);
        ClientCompatibilityManager.INSTANCE.onPublishServer(this::onPublishServer);

        ClientCompatibilityManager.INSTANCE.onVoiceChatConnected(connection -> {
            if (client != null) {
                client.onVoiceChatConnected(connection);
            }
        });
        ClientCompatibilityManager.INSTANCE.onVoiceChatDisconnected(() -> {
            if (client != null) {
                client.onVoiceChatDisconnected();
            }
        });

        ((ClientChannel<SecretPacket>) CommonCompatibilityManager.INSTANCE.getNetManager().secretChannel).setClientListener((client, handler, packet) -> authenticate(packet));
    }

    private void authenticate(SecretPacket secretPacket) {
        if (client == null) {
            Voicechat.LOGGER.error("Received secret without a client being present");
            return;
        }
        Voicechat.LOGGER.info("Received secret");
        if (client.getConnection() != null) {
            ClientCompatibilityManager.INSTANCE.emitVoiceChatDisconnectedEvent();
        }
        NetClientHandler connection = minecraft.func_20001_q();
        if (connection != null) {
            try {
                SocketAddress socketAddress = ClientCompatibilityManager.INSTANCE.getSocketAddress(((NetClientHandlerExtension) connection).getNetworkManager());
                if (socketAddress instanceof InetSocketAddress) {
                    InetSocketAddress address = (InetSocketAddress) socketAddress;
                    client.connect(new InitializationData(address.getHostString(), secretPacket));
                }
            } catch (Exception e) {
                Voicechat.LOGGER.error("Failed to determine server address", e);
            }
        }
    }

    private void onJoinWorld() {
        if (client != null) {
            Voicechat.LOGGER.info("Disconnecting from previous connection due to server change");
            onDisconnect();
        }
        Voicechat.LOGGER.info("Sending secret request to the server");
        ClientNetManager.sendToServer(new RequestSecretPacket(Voicechat.COMPATIBILITY_VERSION));
        client = new ClientVoicechat();
    }

    public static void sendPlayerError(String translationKey, @Nullable Exception e) {
        MinecraftAccessor.getMinecraft().ingameGUI.addChatMessage(StringTranslate.getInstance().translateKeyFormat(translationKey, e.getMessage()));
    }

    private void onDisconnect() {
        if (client != null) {
            client.close();
            client = null;
        }
        ClientCompatibilityManager.INSTANCE.emitVoiceChatDisconnectedEvent();
    }

    private void onPublishServer(int port) {
        Server server = Voicechat.SERVER.getServer();
        if (server == null) {
            return;
        }
        try {
            Voicechat.LOGGER.info("Changing voice chat port to {}", port);
            server.changePort(port);
            ClientVoicechat client = ClientManager.getClient();
            if (client != null) {
                ClientVoicechatConnection connection = client.getConnection();
                if (connection != null) {
                    Voicechat.LOGGER.info("Force disconnecting due to port change");
                    connection.disconnect();
                }
            }
            ClientNetManager.sendToServer(new RequestSecretPacket(Voicechat.COMPATIBILITY_VERSION));
        } catch (Exception e) {
            Voicechat.LOGGER.error("Failed to change voice chat port: {}", e.getMessage());
        }
        MinecraftAccessor.getMinecraft().ingameGUI.addChatMessage(String.format(StringTranslate.getInstance().translateKey("message.voicechat.server_port"), server.getPort()));
    }

    @Nullable
    public static ClientVoicechat getClient() {
        return instance().client;
    }

    public static ClientPlayerStateManager getPlayerStateManager() {
        return instance().playerStateManager;
    }

    public static ClientGroupManager getGroupManager() {
        return instance().groupManager;
    }

    public static ClientCategoryManager getCategoryManager() {
        return instance().categoryManager;
    }

    public static PTTKeyHandler getPttKeyHandler() {
        return instance().pttKeyHandler;
    }

    public static RenderEvents getRenderEvents() {
        return instance().renderEvents;
    }

    public KeyEvents getKeyEvents() {
        return keyEvents;
    }

    private static ClientManager instance;

    public static ClientManager instance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

}
