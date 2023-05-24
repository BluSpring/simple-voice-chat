package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.EntityPlayerExtension;
import net.minecraft.src.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin implements EntityPlayerExtension {
    @Accessor
    public abstract String getUsername();

    private UUID uuid = null;

    @Override
    public UUID getUniqueID() {
        if (uuid == null)
            uuid = UUID.nameUUIDFromBytes(this.getUsername().getBytes(StandardCharsets.UTF_8));
        return uuid;
    }
}

