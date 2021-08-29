package io.github.simplycmd.camping.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.simplycmd.camping.Main;

@Mixin(FishingBobberEntity.class)
public class LiveFishingMixin {
    @ModifyArg(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            index = 0
    )
    private Entity spawnFish(Entity original) {
        FishingBobberEntity self = ((FishingBobberEntity) (Object) this);
        ItemEntity originalItem = ((ItemEntity) original);
        Item item = originalItem.getStack().getItem();
        FishEntity fish;

        // Hardcoded because I couldn't think of a better way
        if (item.equals(Items.COD))
            fish = new CodEntity(EntityType.COD, self.world);
        else if (item.equals(Items.SALMON))
            fish = new SalmonEntity(EntityType.SALMON, self.world);
        else if (item.equals(Items.TROPICAL_FISH))
            fish = new TropicalFishEntity(EntityType.TROPICAL_FISH, self.world);
        else if (item.equals(Items.PUFFERFISH))
            fish = new PufferfishEntity(EntityType.PUFFERFISH, self.world);
        else
            return original;

        PlayerEntity playerEntity = self.getPlayerOwner();
        fish.updatePosition(self.getX(), self.getY(), self.getZ());
        double d = playerEntity.getX() - self.getX();
        double e = playerEntity.getY() - self.getY();
        double f = playerEntity.getZ() - self.getZ();
        double yeetAmount = 0.5F; // How much to yeet the fish

        // Scary math by Mojank (not me)
        fish.setVelocity(d * 0.1D, e * yeetAmount * 0.5 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * 0.1D);

        return fish;
    }

    @Inject(method = "use", at = @At("RETURN"))
    private void removeBobber(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        ((FishingBobberEntity) (Object) this).kill();
    }
}
