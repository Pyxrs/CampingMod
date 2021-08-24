package io.github.simplycmd.camping.mixin;

import io.github.simplycmd.camping.Main;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class HotSpringWaterColorMixin {
    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    private static void getWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (world.getBlockState(pos).getBlock().equals(Main.HOT_SPRING_WATER)) cir.setReturnValue(0xA0CCF2);
    }
}
