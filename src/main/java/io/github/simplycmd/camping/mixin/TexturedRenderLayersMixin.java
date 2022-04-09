package io.github.simplycmd.camping.mixin;

import io.github.simplycmd.camping.Main;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.render.TexturedRenderLayers.SIGNS_ATLAS_TEXTURE;

@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
    @Inject(method = "createSignTextureId", at = @At("HEAD"), cancellable = true)
    private static void createSignTextureId(SignType type, CallbackInfoReturnable<SpriteIdentifier> cir) {
        //System.out.println(type.getName());
        if (type == Main.PINE_SIGN_TYPE)
        cir.setReturnValue(new SpriteIdentifier(SIGNS_ATLAS_TEXTURE, new Identifier("camping:entity/signs/pine")));
    }
}
