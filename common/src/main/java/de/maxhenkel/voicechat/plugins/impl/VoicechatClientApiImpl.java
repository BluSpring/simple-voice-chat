package de.maxhenkel.voicechat.plugins.impl;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.Position;
import de.maxhenkel.voicechat.api.VoicechatClientApi;
import de.maxhenkel.voicechat.api.VolumeCategory;
import de.maxhenkel.voicechat.api.audiochannel.ClientEntityAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.ClientLocationalAudioChannel;
import de.maxhenkel.voicechat.api.audiochannel.ClientStaticAudioChannel;
import de.maxhenkel.voicechat.plugins.impl.audiochannel.ClientEntityAudioChannelImpl;
import de.maxhenkel.voicechat.plugins.impl.audiochannel.ClientLocationalAudioChannelImpl;
import de.maxhenkel.voicechat.plugins.impl.audiochannel.ClientStaticAudioChannelImpl;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManagerApi;
import de.maxhenkel.voicechat.voice.common.ClientGroup;

import javax.annotation.Nullable;
import java.util.UUID;

public class VoicechatClientApiImpl extends VoicechatApiImpl implements VoicechatClientApi {

    private static final VoicechatClientApiImpl INSTANCE = new VoicechatClientApiImpl();

    private VoicechatClientApiImpl() {

    }

    public static VoicechatClientApiImpl instance() {
        return INSTANCE;
    }

    @Override
    public boolean isMuted() {
        return Voicechat.clientInstance.getPlayerStateManager().isMuted();
    }

    @Override
    public boolean isDisabled() {
        return Voicechat.clientInstance.getPlayerStateManager().isDisabled();
    }

    @Override
    public boolean isDisconnected() {
        return Voicechat.clientInstance.getPlayerStateManager().isDisconnected();
    }

    @Override
    @Nullable
    public Group getGroup() {
        ClientPlayerStateManagerApi playerStateManager = Voicechat.clientInstance.getPlayerStateManager();
        if (playerStateManager.getGroupID() == null) {
            return null;
        }
        ClientGroup group = playerStateManager.getGroup();
        if (group == null) {
            return null;
        }
        return new ClientGroupImpl(group);
    }

    @Override
    public ClientEntityAudioChannel createEntityAudioChannel(UUID uuid) {
        return new ClientEntityAudioChannelImpl(uuid);
    }

    @Override
    public ClientLocationalAudioChannel createLocationalAudioChannel(UUID uuid, Position position) {
        return new ClientLocationalAudioChannelImpl(uuid, position);
    }

    @Override
    public ClientStaticAudioChannel createStaticAudioChannel(UUID uuid) {
        return new ClientStaticAudioChannelImpl(uuid);
    }

    @Override
    public void unregisterClientVolumeCategory(String categoryId) {
        Voicechat.clientInstance.getCategoryManager().removeCategory(categoryId);
    }

    @Override
    public void registerClientVolumeCategory(VolumeCategory category) {
        if (!(category instanceof VolumeCategoryImpl)) {
            throw new IllegalArgumentException("VolumeCategory is not an instance of VolumeCategoryImpl");
        }
        VolumeCategoryImpl c = (VolumeCategoryImpl) category;
        Voicechat.clientInstance.getCategoryManager().addCategory(c);
    }
}
