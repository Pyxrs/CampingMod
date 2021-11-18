package io.github.simplycmd.camping.blocks;

public class SleepingBagBlockOld {
    /* TODO: Breaks if no block underneath
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Item dyeItem = player.getStackInHand(hand).getItem();
        if (dyeItem instanceof DyeItem) {
            world.setBlockState(pos, state.with(COLOR, ((DyeItem) dyeItem).getColor()));
            return ActionResult.SUCCESS;
        }
        if (world.isClient) {
            return ActionResult.CONSUME;
        } else {
            pos = pos.offset(state.get(FACING));
            state = world.getBlockState(pos);
            if (!state.isOf(this)) {
                return ActionResult.CONSUME;
            }

            if (isOverworld(world)) {
                player.trySleep(pos).ifLeft((reason) -> {
                    if (reason != null) {
                        player.sendMessage(reason.toText(), true);
                    }
                });
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    /*@Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).getMaterial().isSolid();
    }*/
}
