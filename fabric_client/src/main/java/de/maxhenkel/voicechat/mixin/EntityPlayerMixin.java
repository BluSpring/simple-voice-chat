package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.EntityPlayerExtension;
import net.minecraft.src.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin implements EntityPlayerExtension {
    @Shadow public String username;
    private UUID uuid = null;

    @Override
    public UUID getUniqueID() {
        if (uuid == null)
            uuid = UUID.nameUUIDFromBytes(this.username.getBytes(StandardCharsets.UTF_8));
        return uuid;
    }
}
