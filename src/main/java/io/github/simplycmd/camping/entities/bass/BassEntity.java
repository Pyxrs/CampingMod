package io.github.simplycmd.camping.entities.bass;

import io.github.simplycmd.camping.Main;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class BassEntity extends SchoolingFishEntity {
    public BassEntity(EntityType<? extends BassEntity> entityType, World world) {
        super(entityType, world);
    }

    public ItemStack getBucketItem() {
        return new ItemStack(Items.COD_BUCKET);
    } // TODO: BASS_BUCKET

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COD_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COD_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_COD_HURT;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }
}
