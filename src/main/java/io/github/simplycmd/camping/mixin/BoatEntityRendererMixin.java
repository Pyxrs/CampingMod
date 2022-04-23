package io.github.simplycmd.camping.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.github.simplycmd.camping.ModBoatVariants;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(BoatEntityRenderer.class)
public class BoatEntityRendererMixin {
    @Shadow @Final @Mutable
    private Map<BoatEntity.Type, Pair<Identifier, BoatEntityModel>> texturesAndModels;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void camping$init(EntityRendererFactory.Context context, CallbackInfo ci) {
        //texturesAndModels =
        var d = new HashMap<>(texturesAndModels);
        d.put(ModBoatVariants.PINE, new Pair<>(new Identifier("camping", "textures/entity/boat/pine.png"), new BoatEntityModel(context.getPart(EntityModelLayers.createBoat(ModBoatVariants.PINE)))));
        texturesAndModels = ImmutableMap.copyOf(d);
    }
}
