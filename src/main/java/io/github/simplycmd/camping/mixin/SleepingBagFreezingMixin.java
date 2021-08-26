package io.github.simplycmd.camping.mixin;

import io.github.simplycmd.camping.Main;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(PlayerEntity.class)
public class SleepingBagFreezingMixin {
    private static HashMap<PlayerEntity, Integer> playerFreezeTicks = new HashMap<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private void sleepingBagFreeze(CallbackInfo ci) {
        PlayerEntity self = ((PlayerEntity) (Object) this);
        if (self.isSleeping() && self.world.getBlockState(self.getBlockPos()).getBlock().equals(Main.SLEEPING_BAG)) {
            playerFreezeTicks.putIfAbsent(self, 300);
        }
        if (!self.isSleeping() && playerFreezeTicks.containsKey(self)) {
            playerFreezeTicks.replace(self, playerFreezeTicks.get(self) - 1);
            self.setFrozenTicks(200);
            if (playerFreezeTicks.get(self) <= 0)
                playerFreezeTicks.remove(self);
        }
    }
}
