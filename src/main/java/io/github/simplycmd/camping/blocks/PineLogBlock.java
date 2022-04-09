package io.github.simplycmd.camping.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import io.github.simplycmd.camping.Main;
import io.github.simplycmd.camping.mixin.AxeItemAccessor;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
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
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public PineLogBlock(FabricBlockSettings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(SAPPY, false).with(ACTIVE, false));
    }


    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        super.appendProperties(stateManager);
        stateManager.add(SAPPY);
        stateManager.add(ACTIVE);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(ACTIVE);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(ACTIVE)) return;
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
        return this.asItem().getDefaultStack();
    }
}
