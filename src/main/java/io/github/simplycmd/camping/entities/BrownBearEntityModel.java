package io.github.simplycmd.camping.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;

@Environment(EnvType.CLIENT)
public class BrownBearEntityModel<T extends BrownBearEntity> extends QuadrupedEntityModel<T> {
    public BrownBearEntityModel(ModelPart root) {
        super(root, true, 16.0F, 4.0F, 2.25F, 2.0F, 24);
        /*textureWidth = 64;
        textureHeight = 64;
        head = new ModelPart(this);
        head.setPivot(0.0F, 11.5F, -17.0F);
        head.setTextureOffset(0, 6).addCuboid(-6.5F, -5.0F, -4.0F, 13.0F, 10.0F, 9.0F, 0.0F, false);
        head.setTextureOffset(45, 16).addCuboid(-3.5F, 0.0F, -6.0F, 7.0F, 5.0F, 2.0F, 0.0F, false);
        head.setTextureOffset(52, 25).addCuboid(-8.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
        head.setTextureOffset(52, 25).addCuboid(3.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);

        body = new ModelPart(this);
        body.setPivot(0.0F, 10.0F, 0.0F);


        rotation = new ModelPart(this);
        rotation.setPivot(0.0F, 0.0F, 0.0F);
        body.addChild(rotation);
        setRotationAngle(rotation, 1.5708F, 0.0F, 0.0F);
        rotation.setTextureOffset(0, 25).addCuboid(-9.5F, -13.0F, -6.5F, 19.0F, 26.0F, 13.0F, 0.0F, false);

        leg4 = new ModelPart(this);
        leg4.setPivot(-5.5F, 15.0F, -9.0F);
        leg4.setTextureOffset(40, 0).addCuboid(8.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);

        leg3 = new ModelPart(this);
        leg3.setPivot(5.5F, 15.0F, -9.0F);
        leg3.setTextureOffset(40, 0).addCuboid(-14.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);

        leg2 = new ModelPart(this);
        leg2.setPivot(-5.5F, 15.0F, 9.0F);
        leg2.setTextureOffset(40, 0).addCuboid(8.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);

        leg1 = new ModelPart(this);
        leg1.setPivot(5.5F, 15.0F, 9.0F);
        leg1.setTextureOffset(40, 0).addCuboid(-14.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);*/
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();

        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 6).cuboid(-6.5F, -5.0F, -4.0F, 13.0F, 10.0F, 9.0F).uv(45, 16).cuboid("nose", -3.5F, 0.0F, -6.0F, 7.0F, 5.0F, 2.0F).uv(52, 25).cuboid("left_ear", 3.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F).uv(52, 25).cuboid("right_ear", -8.5F, -8.0F, -1.0F, 5.0F, 4.0F, 1.0F), ModelTransform.pivot(0.0F, 11.5F, -17.0F));
        modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 25).cuboid(-9.5F, -13.0F, -6.5F, 19.0F, 26.0F, 13.0F), ModelTransform.of(0.0F, 10.0F, 0.0F, 1.5707964F, 0.0F, 0.0F));

        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(40, 0).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F);
        modelPartData.addChild("right_hind_leg", modelPartBuilder, ModelTransform.pivot(-5.5F, 15.0F, 9.0F));
        modelPartData.addChild("left_hind_leg", modelPartBuilder, ModelTransform.pivot(5.5F, 15.0F, 9.0F));
        modelPartData.addChild("right_front_leg", modelPartBuilder, ModelTransform.pivot(-5.5F, 15.0F, -9.0F));
        modelPartData.addChild("left_front_leg", modelPartBuilder, ModelTransform.pivot(5.5F, 15.0F, -9.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public void setAngles(T brownBearEntity, float f, float g, float h, float i, float j) {
        super.setAngles(brownBearEntity, f, g, h, i, j);
    }
}
