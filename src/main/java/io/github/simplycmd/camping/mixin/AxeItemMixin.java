package io.github.simplycmd.camping.mixin;

import io.github.simplycmd.camping.Main;
import io.github.simplycmd.camping.blocks.PineLogBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        if (state.getBlock().equals(Main.PINE_LOG))
            if (state.get(PineLogBlock.SAPPY)) {
                // Remove sap
                context.getWorld().setBlockState(context.getBlockPos(), state.with(PineLogBlock.SAPPY, false));
                context.getWorld().playSound(context.getPlayer(), context.getBlockPos(), SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                cir.setReturnValue(ActionResult.SUCCESS);
            } else {
                // Had to manually implement log stripping since log types are hardcoded
                context.getWorld().setBlockState(context.getBlockPos(), Blocks.STRIPPED_SPRUCE_LOG.getDefaultState());
                context.getWorld().playSound(context.getPlayer(), context.getBlockPos(), SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                cir.setReturnValue(ActionResult.SUCCESS);
            }
    }
}
