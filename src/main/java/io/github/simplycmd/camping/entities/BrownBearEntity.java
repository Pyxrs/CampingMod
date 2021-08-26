package io.github.simplycmd.camping.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class BrownBearEntity extends PolarBearEntity {
    public BrownBearEntity(EntityType<? extends BrownBearEntity> entityType, World world) {
        super(entityType, world);
    }
}
