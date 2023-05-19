package de.maxhenkel.voicechat.intercompatibility;

import de.maxhenkel.voicechat.MinecraftAccessor;
import de.maxhenkel.voicechat.extensions.NetClientHandlerExtension;
import de.maxhenkel.voicechat.mixin.NetworkManagerAccessor;
import de.maxhenkel.voicechat.voice.client.ClientVoicechatConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.NetworkManager;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FabricClientCompatibilityManager extends ClientCompatibilityManager {
    private static FabricClientCompatibilityManager instance;
    private Minecraft minecraft;

    private final List<RenderNameplateEvent> renderNameplateEvents;
    private final List<RenderHUDEvent> renderHUDEvents;
    private final List<KeyboardEvent> keyboardEvents;
    private final List<MouseEvent> mouseEvents;
    private final List<Runnable> inputEvents;
    private final List<Runnable> disconnectEvents;
    private final List<Runnable> joinServerEvents;
    private final List<Runnable> joinWorldEvents;
    private final List<Consumer<ClientVoicechatConnection>> voicechatConnectEvents;
    private final List<Runnable> voicechatDisconnectEvents;
    private final List<Consumer<Integer>> publishServerEvents;

    public static FabricClientCompatibilityManager getInstance() {
        return instance;
    }

    public FabricClientCompatibilityManager() {
        instance = this;
        minecraft = MinecraftAccessor.getMinecraft();
        renderNameplateEvents = new ArrayList<>();
        renderHUDEvents = new ArrayList<>();
        keyboardEvents = new ArrayList<>();
        mouseEvents = new ArrayList<>();
        inputEvents = new ArrayList<>();
        disconnectEvents = new ArrayList<>();
        joinServerEvents = new ArrayList<>();
        joinWorldEvents = new ArrayList<>();
        voicechatConnectEvents = new ArrayList<>();
        voicechatDisconnectEvents = new ArrayList<>();
        publishServerEvents = new ArrayList<>();
    }

    public void onRenderName(Entity entity, String str, double x, double y, double z, int maxDistance) {
        renderNameplateEvents.forEach(renderNameplateEvent -> renderNameplateEvent.render(entity, str, x, y, z, maxDistance));
        //TODO Check if player can be seen
        if (minecraft == null)
            minecraft = MinecraftAccessor.getMinecraft();

        if (minecraft.thePlayer == null /*|| entity.isInvisibleTo(minecraft.player)*/) {
            return;
        }
        renderNameplateEvents.forEach(renderNameplateEvent -> renderNameplateEvent.render(entity, str, x, y, z, maxDistance));
    }

    public void onTickKey() {
        keyboardEvents.forEach(KeyboardEvent::onKeyboardEvent);
    }

    public void onTickMouse() {
        mouseEvents.forEach(MouseEvent::onMouseEvent);
    }

    public void onInput() {
        inputEvents.forEach(Runnable::run);
    }

    public void onDisconnect() {
        disconnectEvents.forEach(Runnable::run);
    }

    public void onJoinServer() {
        joinServerEvents.forEach(Runnable::run);
        joinWorldEvents.forEach(Runnable::run);
    }

    public void onJoinWorld(Entity entity) {
        if (minecraft == null)
            minecraft = MinecraftAccessor.getMinecraft();

        if (entity != minecraft.thePlayer) {
            return;
        }
        joinWorldEvents.forEach(Runnable::run);
    }

    public void onOpenPort(int port) {
        publishServerEvents.forEach(consumer -> consumer.accept(port));
    }

    @Override
    public void onRenderNamePlate(RenderNameplateEvent onRenderNamePlate) {
        renderNameplateEvents.add(onRenderNamePlate);
    }

    @Override
    public void onRenderHUD(RenderHUDEvent onRenderHUD) {
        renderHUDEvents.add(onRenderHUD);
    }

    @Override
    public void onKeyboardEvent(KeyboardEvent onKeyboardEvent) {
        keyboardEvents.add(onKeyboardEvent);
    }

    @Override
    public void onMouseEvent(MouseEvent onMouseEvent) {
        mouseEvents.add(onMouseEvent);
    }

    @Override
    public int getBoundKeyOf(KeyBinding keyBinding) {
        return keyBinding.keyCode;
    }

    @Override
    public void onHandleKeyBinds(Runnable onHandleKeyBinds) {
        inputEvents.add(onHandleKeyBinds);
    }

    @Override
    public KeyBinding registerKeyBinding(KeyBinding keyBinding, String translation) {
        if (minecraft == null)
            minecraft = MinecraftAccessor.getMinecraft();

        KeyBinding[] oldArray = minecraft.gameSettings.keyBindings;
        KeyBinding[] newArray = Arrays.copyOf(oldArray, oldArray.length + 1);
        newArray[oldArray.length] = keyBinding;

        minecraft.gameSettings.keyBindings = newArray;

        return keyBinding;
    }

    @Override
    public void emitVoiceChatConnectedEvent(ClientVoicechatConnection client) {
        voicechatConnectEvents.forEach(consumer -> consumer.accept(client));
        //MinecraftForge.EVENT_BUS.post(new ClientVoiceChatConnectedEvent(client));
    }

    @Override
    public void emitVoiceChatDisconnectedEvent() {
        voicechatDisconnectEvents.forEach(Runnable::run);
        //MinecraftForge.EVENT_BUS.post(new ClientVoiceChatDisconnectedEvent());
    }

    @Override
    public void onVoiceChatConnected(Consumer<ClientVoicechatConnection> onVoiceChatConnected) {
        voicechatConnectEvents.add(onVoiceChatConnected);
    }

    @Override
    public void onVoiceChatDisconnected(Runnable onVoiceChatDisconnected) {
        voicechatDisconnectEvents.add(onVoiceChatDisconnected);
    }

    @Override
    public void onDisconnect(Runnable onDisconnect) {
        disconnectEvents.add(onDisconnect);
    }

    @Override
    public void onJoinServer(Runnable onJoinServer) {
        joinServerEvents.add(onJoinServer);
    }

    @Override
    public void onJoinWorld(Runnable onJoinWorld) {
        joinWorldEvents.add(onJoinWorld);
    }

    @Override
    public void onPublishServer(Consumer<Integer> onPublishServer) {
        publishServerEvents.add(onPublishServer);
    }

    @Override
    public SocketAddress getSocketAddress(NetworkManager connection) {
        return ((NetworkManagerAccessor) connection).getNetworkSocket().getRemoteSocketAddress();
    }
}
