package io.github.simplycmd.camping;

import io.github.simplycmd.camping.entities.bass.BassEntityModel;
import io.github.simplycmd.camping.entities.bass.BassEntityRenderer;
import io.github.simplycmd.camping.entities.bear.BrownBearEntityModel;
import io.github.simplycmd.camping.entities.bear.BrownBearEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MainClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_BROWN_BEAR_LAYER = new EntityModelLayer(new Identifier(Main.MOD_ID, "brown_bear"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(Main.BROWN_BEAR, BrownBearEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_BROWN_BEAR_LAYER, BrownBearEntityModel::getTexturedModelData);
        EntityRendererRegistry.INSTANCE.register(Main.BASS, BassEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(Main.SLEEPING_BAG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Main.SLEEPING_BAG, RenderLayer.getTranslucent());
    }
}
