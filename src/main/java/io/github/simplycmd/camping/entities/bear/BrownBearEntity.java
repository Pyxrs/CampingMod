package io.github.simplycmd.camping.entities.bear;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.world.World;

public class BrownBearEntity extends PolarBearEntity {
    public BrownBearEntity(EntityType<? extends BrownBearEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 0.05F;
    }
}
