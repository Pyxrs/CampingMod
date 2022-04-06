package io.github.simplycmd.camping.mixin;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeConfiguredFeatures.class)
public interface TreeConfiguredFeaturesAccessor {
    @Invoker("builder")
    static TreeFeatureConfig.Builder builder(Block log, Block leaves, int baseHeight, int firstRandomHeight, int secondRandomHeight, int radius) {
        throw new UnsupportedOperationException();
    }
}
