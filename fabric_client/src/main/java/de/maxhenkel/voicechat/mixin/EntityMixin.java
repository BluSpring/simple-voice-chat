package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.extensions.EntityExtension;
import net.minecraft.src.Entity;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

@Mixin(Entity.class)
public class EntityMixin implements EntityExtension {
    private final UUID uuid = UUID.randomUUID();

    @Override
    public UUID getUniqueID() {
        return uuid;
    }
}
