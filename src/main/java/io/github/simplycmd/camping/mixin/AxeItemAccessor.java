package io.github.simplycmd.camping.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {
    @Accessor
    static Map<Block, Block> getSTRIPPED_BLOCKS() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static void setSTRIPPED_BLOCKS(Map<Block, Block> STRIPPED_BLOCKS) {
        throw new UnsupportedOperationException();
    }
}
