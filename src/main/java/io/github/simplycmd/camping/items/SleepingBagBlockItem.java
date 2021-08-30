package io.github.simplycmd.camping.items;

import io.github.simplycmd.camping.Main;
import io.github.simplycmd.camping.blocks.SleepingBagBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SleepingBagBlockItem extends BlockItem {
    public SleepingBagBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText("Can be colored by using").formatted(Formatting.ITALIC, Formatting.GRAY));
        tooltip.add(new LiteralText("any dye item on the block").formatted(Formatting.ITALIC, Formatting.GRAY));
    }
}
