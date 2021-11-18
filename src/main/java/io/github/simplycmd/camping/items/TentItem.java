package io.github.simplycmd.camping.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import io.github.simplycmd.camping.blocks.SleepingBagBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class TentItem extends Item {
    public TentItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockHitResult hit = raycast(context.getWorld(), context.getPlayer(), RaycastContext.FluidHandling.NONE);
        BlockState state = context.getWorld().getBlockState(hit.getBlockPos());
        ItemStack itemStack = context.getPlayer().getStackInHand(context.getHand());
        if (state.getBlock() instanceof SleepingBagBlock) {
            context.getWorld().setBlockState(hit.getBlockPos(), state.with(SleepingBagBlock.TENT, true));
            itemStack.decrement(1);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText("Sneak and place on a sleeping").formatted(Formatting.ITALIC, Formatting.GRAY));
        tooltip.add(new LiteralText("bag to protect it from cold").formatted(Formatting.ITALIC, Formatting.GRAY));
    }
}
