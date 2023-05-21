package de.maxhenkel.voicechat.voice.client;

import de.maxhenkel.voicechat.api.VolumeCategory;

public interface ClientCategoryManagerApi {
    void addCategory(VolumeCategory category);
    VolumeCategory removeCategory(String categoryId);
}
