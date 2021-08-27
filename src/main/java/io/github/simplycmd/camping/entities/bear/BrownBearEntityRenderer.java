package io.github.simplycmd.camping.entities.bear;

import io.github.simplycmd.camping.Main;
import io.github.simplycmd.camping.MainClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BrownBearEntityRenderer extends MobEntityRenderer<BrownBearEntity, BrownBearEntityModel<BrownBearEntity>> {
    public BrownBearEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BrownBearEntityModel(context.getPart(MainClient.MODEL_BROWN_BEAR_LAYER)), 0.9F);
    }

    @Override
    public Identifier getTexture(BrownBearEntity entity) {
        return new Identifier(Main.MOD_ID, "textures/entity/brown_bear/brown_bear.png");
    }
}