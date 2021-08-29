package io.github.simplycmd.camping.entities.bear;

import io.github.simplycmd.camping.Main;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class BrownBearEntity extends PolarBearEntity {
    public BrownBearEntity(EntityType<? extends BrownBearEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createBrownBearAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return Main.BROWN_BEAR.create(world);
    }

    @Override
    public boolean shouldAngerAt(LivingEntity livingEntity) {
        if (!this.canTarget(livingEntity)) {
            return false;
        } else {
            return livingEntity.getType().equals(EntityType.PLAYER) || livingEntity.getUuid().equals(this.getAngryAt());
        }
    }
}
