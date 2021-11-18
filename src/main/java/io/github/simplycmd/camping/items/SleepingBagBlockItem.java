package io.github.simplycmd.camping.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

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
