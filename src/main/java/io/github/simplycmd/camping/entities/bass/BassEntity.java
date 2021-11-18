package io.github.simplycmd.camping.entities.bass;

import io.github.simplycmd.camping.Main;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class BassEntity extends FishEntity {
    public BassEntity(EntityType<? extends BassEntity> entityType, World world) {
        super(entityType, world);
    }

    public ItemStack getBucketItem() {
        return Main.BASS_BUCKET.getDefaultStack();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SALMON_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SALMON_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SALMON_HURT;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_SALMON_FLOP;
    }

    public static DefaultAttributeContainer.Builder createBassAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0D);
    }
}
