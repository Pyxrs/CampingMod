package io.github.simplycmd.camping.mixin;

import io.github.simplycmd.camping.Main;
import io.github.simplycmd.camping.ModBoatVariants;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(BoatEntity.Type.class)
public class BoatEntityTypeMixin {

    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static BoatEntity.Type createType(String internalName, int internalId, Block baseBlock, String name) {
        throw new AssertionError();
    }

    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    @Mutable
    private static BoatEntity.Type[] field_7724;
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "FIELD",
            opcode = Opcodes.PUTSTATIC,
            target = "Lnet/minecraft/entity/vehicle/BoatEntity$Type;field_7724:[Lnet/minecraft/entity/vehicle/BoatEntity$Type;",
            shift = At.Shift.AFTER), remap = false)
    private static void addCustomType(CallbackInfo ci) {
        //Lnet/minecraft/block/entity/CampfireBlockEntity;litServerTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/CampfireBlockEntity;)V
        var variants = new ArrayList<>(Arrays.asList(field_7724));
        var last = variants.get(variants.size() - 1);


        var pine = createType("PINE", last.ordinal() + 1, Main.PINE_PLANKS, "camping_pine");
        ModBoatVariants.PINE = pine;
        variants.add(pine);

        field_7724 = variants.toArray(new BoatEntity.Type[0]);
    }
}
