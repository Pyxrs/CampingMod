package io.github.simplycmd.camping.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.blockpredicate.WouldSurviveBlockPredicate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WouldSurviveBlockPredicate.class)
public class W {
    @Shadow @Final private BlockState state;

    @Shadow @Final private Vec3i offset;

    @Overwrite
    public boolean test(StructureWorldAccess structureWorldAccess, BlockPos blockPos) /*throws Exception*/ {
        System.out.println(blockPos.toString());
        //throw new Exception();
        return this.state.canPlaceAt(structureWorldAccess, blockPos.add(this.offset));
    }
}
