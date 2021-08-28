package io.github.simplycmd.camping.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.simplycmd.camping.Main;

@Mixin(CampfireBlockEntity.class)
public class CampfireComfortMixin {
    private static final int HEALING_RADIUS = 5;
    private static int tick;

    @Inject(at = @At("TAIL"), method = "litServerTick")
    private static void litServerTick(World world, BlockPos pos, BlockState state, CampfireBlockEntity campfire, CallbackInfo ci) {
        for (PlayerEntity player : world.getPlayers()) {
            BlockPos player_pos = player.getBlockPos();
            if (Math.abs(pos.getX() - player_pos.getX()) < HEALING_RADIUS)
                if (Math.abs(pos.getY() - player_pos.getY()) < HEALING_RADIUS)
                    if (Math.abs(pos.getZ() - player_pos.getZ()) < HEALING_RADIUS) {
                        tick++;
                        if (tick > 100) {
                            tick = 0;
                            player.heal(1);
                        }

                        player.addStatusEffect(new StatusEffectInstance(Main.COZINESS, 10, 0, true, false, true));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 10, 0, true, false, true));
                    }
        }
    }
}
