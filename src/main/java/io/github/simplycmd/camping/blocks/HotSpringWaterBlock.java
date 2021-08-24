package io.github.simplycmd.camping.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class HotSpringWaterBlock extends BubbleColumnBlock {
    int tick;

    public HotSpringWaterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            tick++;
            if (tick > 100) {
                tick = 0;
                ((LivingEntity) entity).heal(1);
            }

            // Having regeneration with this small of a duration doesn't actually give the effect but shows the icon, which is great
            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 10, 0, false, false, true));
        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        world.addParticle(ParticleTypes.SPLASH, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.BUBBLE, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.00D);
        if (random.nextFloat() > 0.5)
            world.addParticle(ParticleTypes.POOF, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
    }
}
