package io.github.simplycmd.camping.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SimpleBlockStateProvider.class)
public interface SimpleBlockStateProviderAccessor {
    @Invoker("<init>")
    static SimpleBlockStateProvider create(BlockState state) {
        throw new UnsupportedOperationException();
    }
}