package io.github.simplycmd.camping.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;

public class PineLogBlock extends PillarBlock {
    private static final int UPDATE_RADIUS = 15;
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
        if (random.nextFloat() > 0.996 && world.getNonSpectatingEntities(PlayerEntity.class, Box.from(BlockBox.create(new Vec3i(pos.getX() - UPDATE_RADIUS, pos.getY() - UPDATE_RADIUS, pos.getZ() - UPDATE_RADIUS), new Vec3i(pos.getX() + UPDATE_RADIUS, pos.getY() + UPDATE_RADIUS, pos.getZ() + UPDATE_RADIUS)))).size() > 0) {
            world.setBlockState(pos, state.with(SAPPY, true));
        }

        // Become un-sappy
        else if (world.getBlockState(pos).get(SAPPY) && world.getNonSpectatingEntities(PlayerEntity.class, Box.from(BlockBox.create(new Vec3i(pos.getX() - UPDATE_RADIUS, pos.getY() - UPDATE_RADIUS, pos.getZ() - UPDATE_RADIUS), new Vec3i(pos.getX() + UPDATE_RADIUS, pos.getY() + UPDATE_RADIUS, pos.getZ() + UPDATE_RADIUS)))).size() <= 0) {
            world.setBlockState(pos, state.with(SAPPY, false));
        }
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return Items.SPRUCE_LOG.getDefaultStack();
    }
}
