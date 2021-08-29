package io.github.simplycmd.camping.entities.bass;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BassEntityModel<T extends Entity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart tailFin;

    public BassEntityModel(ModelPart root) {
        this.root = root;
        this.tailFin = root.getChild("tail_fin");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 0.0F, -7.0F, 4.0F, 8.0F, 12.0F), ModelTransform.pivot(0.0F, 22.0F, 0.0F));
        modelPartData.addChild("body_back", ModelPartBuilder.create().uv(20, 0).cuboid(-1.5F, 2.0F, 5.0F, 3.0F, 5.0F, 4.0F), ModelTransform.pivot(0.0F, 22.0F, 0.0F));
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 20).cuboid(-2.0F, 2.0F, -11.0F, 4.0F, 6.0F, 4.0F), ModelTransform.pivot(0.0F, 22.0F, 0.0F));
        modelPartData.addChild("tail_fin", ModelPartBuilder.create().uv(16, 14).cuboid(0.0F, 1.0F, 0.0F, 0.0F, 7.0F, 6.0F), ModelTransform.pivot(0.0F, 22.0F, 9.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public ModelPart getPart() {
        return this.root;
    }

    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float f = 1.0F;
        if (!entity.isTouchingWater()) {
            f = 1.5F;
        }

        this.tailFin.yaw = -f * 0.45F * MathHelper.sin(0.6F * animationProgress);
    }
}

