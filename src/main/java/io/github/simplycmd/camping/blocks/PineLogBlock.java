package io.github.simplycmd.camping.blocks;

import io.github.simplycmd.camping.Main;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.Random;

public class PineLogBlock extends PillarBlock {
    public static final BooleanProperty SAPPY = BooleanProperty.of("sappy");

    public PineLogBlock(FabricBlockSettings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(SAPPY, false));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        super.appendProperties(stateManager);
        stateManager.add(SAPPY);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Become sappy
        if (random.nextFloat() > 0.999) {
            world.setBlockState(pos, state.with(SAPPY, true));
        }

        // Become un-sappy
        else if (random.nextFloat() > 0.5 && world.getBlockState(pos).get(SAPPY)) {
            world.setBlockState(pos, state.with(SAPPY, false));
        }
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return Items.SPRUCE_LOG.getDefaultStack();
    }
}
