package de.maxhenkel.voicechat.mixin;

import de.maxhenkel.voicechat.Voicechat;
import net.minecraft.src.StringTranslate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.Properties;

@Mixin(StringTranslate.class)
public class StringTranslateMixin {
    private final Properties voiceChatLangTable = new Properties();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void loadVoiceChatLang(CallbackInfo ci) {
        try {
            this.voiceChatLangTable.load(Voicechat.class.getResourceAsStream("/assets/voicechat/lang/en_us.lang"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "translateKey", at = @At("HEAD"), cancellable = true)
    public void useVoiceChatLangTranslation(String par1, CallbackInfoReturnable<String> cir) {
        String translation = voiceChatLangTable.getProperty(par1, par1);

        if (!translation.equals(par1))
            cir.setReturnValue(par1);
    }
}
