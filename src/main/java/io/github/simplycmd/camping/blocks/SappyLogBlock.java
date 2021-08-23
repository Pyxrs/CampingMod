package io.github.simplycmd.camping.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class SappyLogBlock extends Block {
    public static final BooleanProperty SAPPY = BooleanProperty.of("sappy");

    public SappyLogBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(SAPPY, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(SAPPY);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(SAPPY, true));
    }
}
