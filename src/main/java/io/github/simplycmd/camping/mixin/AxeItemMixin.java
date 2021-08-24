package io.github.simplycmd.camping.mixin;

import io.github.simplycmd.camping.Main;
import io.github.simplycmd.camping.blocks.PineLogBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    private static final Random RANDOM = new Random();

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(pos);
        if (state.getBlock().equals(Main.PINE_LOG))
            if (state.get(PineLogBlock.SAPPY)) {
                // Remove sap
                context.getWorld().setBlockState(pos, state.with(PineLogBlock.SAPPY, false));
                context.getWorld().playSound(context.getPlayer(), pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);

                // Spawn sap item
                ItemStack sap = Main.SAP.getDefaultStack();
                sap.setCount(RANDOM.nextInt(2) + 1); // Random sap amount between 1 and 3
                ItemEntity sapEntity = new ItemEntity(context.getWorld(), pos.getX(), pos.getY(), pos.getZ(), sap);
                sapEntity.updatePosition(pos.getX(), pos.getY(), pos.getZ());
                context.getWorld().spawnEntity(sapEntity);

                // Damage axe
                damageAxe(context);

                // Make axe do that slurp thing
                cir.setReturnValue(ActionResult.SUCCESS);
            } else {
                // Had to manually implement log stripping since log types are hardcoded
                context.getWorld().setBlockState(pos, Blocks.STRIPPED_SPRUCE_LOG.getDefaultState());
                context.getWorld().playSound(context.getPlayer(), pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                damageAxe(context);
                cir.setReturnValue(ActionResult.SUCCESS);
            }
    }

    private static void damageAxe(ItemUsageContext context) {
        if (context.getPlayer() != null)
            context.getStack().damage(1, context.getPlayer(), (p) -> p.sendToolBreakStatus(context.getHand()));
    }
}
