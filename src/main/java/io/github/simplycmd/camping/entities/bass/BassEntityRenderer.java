package io.github.simplycmd.camping.entities.bass;

import io.github.simplycmd.camping.Main;
import io.github.simplycmd.camping.MainClient;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CodEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class BassEntityRenderer extends MobEntityRenderer<BassEntity, BassEntityModel<BassEntity>> {

    public BassEntityRenderer(Context context) {
        super(context, new BassEntityModel<>(context.getPart(MainClient.MODEL_BASS_LAYER)), 0.3F);
    }

    @Override
    public Identifier getTexture(BassEntity entity) {
        return new Identifier(Main.MOD_ID, "textures/entity/fish/bass.png");
    }

    protected void setupTransforms(BassEntity bassEntity, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(bassEntity, matrixStack, f, g, h);
        float i = 4.3F * MathHelper.sin(0.6F * f);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(i));
        if (!bassEntity.isTouchingWater()) {
            matrixStack.translate(0.10000000149011612D, 0.10000000149011612D, -0.10000000149011612D);
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
        }
    }
}