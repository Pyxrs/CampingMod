package io.github.simplycmd.camping.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.explosion.Explosion;

public class SleepingBagBlock extends HorizontalFacingBlock {
    public static final EnumProperty<BedPart> PART = Properties.BED_PART;
    public static final BooleanProperty OCCUPIED = Properties.OCCUPIED;
    public static final BooleanProperty TENT = BooleanProperty.of("tent");
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);

    public SleepingBagBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(PART, BedPart.FOOT)
                .with(OCCUPIED, false)
                .with(TENT, false)
                .with(COLOR, DyeColor.WHITE));
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Item dyeItem = player.getStackInHand(hand).getItem();
        if (dyeItem instanceof DyeItem) {
            world.setBlockState(pos, state.with(COLOR, ((DyeItem) dyeItem).getColor()));
            return ActionResult.SUCCESS;
        }
        if (world.isClient) {
            return ActionResult.CONSUME;
        } else {
            if (state.get(PART) != BedPart.HEAD) {
                pos = pos.offset(state.get(FACING));
                state = world.getBlockState(pos);
                if (!state.isOf(this)) {
                    return ActionResult.CONSUME;
                }
            }

            if (!isOverworld(world)) {
                world.removeBlock(pos, false);
                BlockPos blockPos = pos.offset(state.get(FACING).getOpposite());
                if (world.getBlockState(blockPos).isOf(this)) {
                    world.removeBlock(blockPos, false);
                }

                world.createExplosion(null, DamageSource.badRespawnPoint(), null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 5.0F, true, Explosion.DestructionType.DESTROY);
                return ActionResult.SUCCESS;
            } else {
                player.trySleep(pos).ifLeft((reason) -> {
                    if (reason != null) {
                        player.sendMessage(reason.toText(), true);
                    }
                });
                return ActionResult.SUCCESS;
            }
        }
    }

    public static boolean isOverworld(World world) {
        return world.getDimension().isBedWorking();
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == getDirectionTowardsOtherPart(state.get(PART), state.get(FACING))) {
            return neighborState.isOf(this) && neighborState.get(PART) != state.get(PART) ? state.with(OCCUPIED, neighborState.get(OCCUPIED)) : Blocks.AIR.getDefaultState();
        } else {
            return direction == Direction.DOWN && !this.canPlaceAt(state, world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            BedPart bedPart = state.get(PART);
            if (bedPart == BedPart.FOOT) {
                BlockPos blockPos = pos.offset(getDirectionTowardsOtherPart(bedPart, state.get(FACING)));
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isOf(this) && blockState.get(PART) == BedPart.HEAD) {
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
                    world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
                }
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(direction);
        return ctx.getWorld().getBlockState(blockPos2).canReplace(ctx) ? this.getDefaultState().with(FACING, direction) : null;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    }

    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED, TENT, COLOR);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockPos blockPos = pos.offset(state.get(FACING));
            world.setBlockState(blockPos, state.with(PART, BedPart.HEAD), 3);
            world.updateNeighbors(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, 3);
        }
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return true;
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).getMaterial().isSolid();
    }
}
