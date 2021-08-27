package io.github.simplycmd.camping.entities.bass;

import io.github.simplycmd.camping.Main;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class BassEntityModel extends AnimatedGeoModel<BassEntity> {
    @Override
    public Identifier getModelLocation(BassEntity object) {
        return new Identifier(Main.MOD_ID, "textures/entity/bass/bass.geo.json");
    }

    @Override
    public Identifier getTextureLocation(BassEntity object) {
        return new Identifier(Main.MOD_ID, "textures/entity/bass/bass.png");
    }

    @Override
    public Identifier getAnimationFileLocation(BassEntity animatable) {
        return null;
    }
}