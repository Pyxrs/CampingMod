package io.github.simplycmd.camping.mixin;

import io.github.simplycmd.camping.Main;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class SleepingBagFreezingMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void sleepingBagFreeze(CallbackInfo ci) {
        PlayerEntity self = ((PlayerEntity) (Object) this);
        if (self.isSleeping() && self.world.getBlockState(self.getBlockPos()).getBlock().equals(Main.SLEEPING_BAG)) {
            self.setFrozenTicks(200);
        }
    }
}
