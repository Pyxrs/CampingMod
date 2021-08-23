package io.github.simplycmd.camping;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CampingTreeFeature extends Feature<DefaultFeatureConfig> {
    public CampingTreeFeature(Codec<DefaultFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos topPos = context.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, context.getOrigin());
        Direction offset = Direction.NORTH;

        for (int y = 1; y <= 15; y++) {
            offset = offset.rotateYClockwise();
            context.getWorld().setBlockState(topPos.up(y).offset(offset), Blocks.STONE.getDefaultState(), 3);
        }

        return true;
    }
}