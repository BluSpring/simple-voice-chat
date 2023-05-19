package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.intercompatibility.ClientCompatibilityManager;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
import de.maxhenkel.voicechat.net.NetManager;
import de.maxhenkel.voicechat.net.RequestSecretPacket;
import de.maxhenkel.voicechat.net.SecretPacket;
import de.maxhenkel.voicechat.voice.server.Server;
import io.netty.channel.local.LocalAddress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;

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
        minecraft = Minecraft.getMinecraft();

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

        CommonCompatibilityManager.INSTANCE.getNetManager().secretChannel.setClientListener((client, handler, packet) -> authenticate(packet));
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
        NetHandlerPlayClient connection = minecraft.getConnection();
        if (connection != null) {
            try {
                SocketAddress socketAddress = ClientCompatibilityManager.INSTANCE.getSocketAddress(connection.getNetworkManager());
                if (socketAddress instanceof InetSocketAddress) {
                    InetSocketAddress address = (InetSocketAddress) socketAddress;
                    client.connect(new InitializationData(address.getHostString(), secretPacket));
                } else if (socketAddress instanceof LocalAddress) {
                    client.connect(new InitializationData("127.0.0.1", secretPacket));
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
        NetManager.sendToServer(new RequestSecretPacket(Voicechat.COMPATIBILITY_VERSION));
        client = new ClientVoicechat();
    }

    public static void sendPlayerError(String translationKey, @Nullable Exception e) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) {
            return;
        }
        Style style = new Style().setColor(TextFormatting.RED);
        if (e != null) {
            style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(e.getMessage()).setStyle(new Style().setColor(TextFormatting.RED))));
        }
        player.sendMessage(
                wrapInSquareBrackets(new TextComponentString(CommonCompatibilityManager.INSTANCE.getModName()))
                        .setStyle(new Style().setColor(TextFormatting.GREEN))
                        .appendText(" ")
                        .appendSibling(new TextComponentTranslation(translationKey).setStyle(style))
        );
    }

    private static ITextComponent wrapInSquareBrackets(ITextComponent component) {
        return new TextComponentString("[").appendSibling(component).appendText("]");
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
            NetManager.sendToServer(new RequestSecretPacket(Voicechat.COMPATIBILITY_VERSION));
        } catch (Exception e) {
            Voicechat.LOGGER.error("Failed to change voice chat port: {}", e.getMessage());
        }
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("message.voicechat.server_port", server.getPort()));
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
