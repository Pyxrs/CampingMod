package io.github.simplycmd.camping.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.simplycmd.camping.blocks.SleepingBagBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public class SleepingBagSpawnpointMixin {
    @Inject(method = "setSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void preventSpawnPoint(RegistryKey<World> dimension, BlockPos pos, float angle, boolean spawnPointSet, boolean sendMessage, CallbackInfo ci) {
        if (((ServerPlayerEntity) (Object) this).world.getBlockState(pos).getBlock() instanceof SleepingBagBlock) {
            ((ServerPlayerEntity) (Object) this).sendSystemMessage(new TranslatableText("spawnpoint.camping.sleeping_bag"), Util.NIL_UUID);
            ci.cancel();
        }
    }
}
