package io.github.simplycmd.camping.mixin;

import io.github.simplycmd.camping.entities.bass.BassEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.simplycmd.camping.Main;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(FishingBobberEntity.class)
public class LiveFishingMixin {
    /*@ModifyArg(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            index = 0
    )*/
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
        else if (item.equals(Main.RAW_BASS))
            fish = new BassEntity(Main.BASS, self.world);
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

    /*@Inject(method = "use", at = @At("RETURN"))
    private void removeBobber(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        ((FishingBobberEntity) (Object) this).remove(Entity.RemovalReason.DISCARDED);
    }*/

    @Shadow
    private Entity hookedEntity;

    @Shadow
    private int hookCountdown;

    @Shadow
    @Final
    private int luckOfTheSeaLevel;

    /**
     * @author SimplyCmd
     * @reason Very invasive fix but it works so im not complaining
     */
    @Overwrite
    public int use(ItemStack usedItem) {
        FishingBobberEntity self = ((FishingBobberEntity) (Object) this);
        PlayerEntity playerEntity = self.getPlayerOwner();
        if (!self.world.isClient && playerEntity != null && !this.removeIfInvalid(playerEntity)) {
            int i = 0;
            if (this.hookedEntity != null) {
                this.pullHookedEntity(this.hookedEntity);
                Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)playerEntity, usedItem, self, Collections.emptyList());
                self.world.sendEntityStatus(self, (byte)31);
                i = this.hookedEntity instanceof ItemEntity ? 3 : 5;
            } else if (this.hookCountdown > 0) {
                LootContext.Builder builder = (new LootContext.Builder((ServerWorld)self.world)).parameter(LootContextParameters.ORIGIN, self.getPos()).parameter(LootContextParameters.TOOL, usedItem).parameter(LootContextParameters.THIS_ENTITY, self).random(self.world.random).luck((float)this.luckOfTheSeaLevel + playerEntity.getLuck());
                LootTable lootTable = self.world.getServer().getLootManager().getTable(LootTables.FISHING_GAMEPLAY);
                List<ItemStack> list = lootTable.generateLoot(builder.build(LootContextTypes.FISHING));
                Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)playerEntity, usedItem, self, list);

                for (ItemStack itemStack : list) {
                    ItemEntity itemEntity = new ItemEntity(self.world, self.getX(), self.getY(), self.getZ(), itemStack);
                    double d = playerEntity.getX() - self.getX();
                    double e = playerEntity.getY() - self.getY();
                    double f = playerEntity.getZ() - self.getZ();
                    double g = 0.1D;
                    itemEntity.setVelocity(d * 0.1D, e * 0.1D + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * 0.1D);
                    self.world.spawnEntity(spawnFish(itemEntity));
                    playerEntity.world.spawnEntity(new ExperienceOrbEntity(playerEntity.world, playerEntity.getX(), playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, self.world.random.nextInt(6) + 1));
                    if (itemStack.isIn(ItemTags.FISHES)) {
                        playerEntity.increaseStat(Stats.FISH_CAUGHT, 1);
                    }
                }

                i = 1;
            }

            //if (this.onGround) {
            //    i = 2;
            //}

            self.discard();
            return i;
        } else {
            return 0;
        }
    }

    public void pullHookedEntity(Entity entity) {
        Entity entity2 = ((FishingBobberEntity) (Object) this).getOwner();
        if (entity2 != null) {
            Vec3d vec3d = (new Vec3d(entity2.getX() - ((FishingBobberEntity) (Object) this).getX(), entity2.getY() - ((FishingBobberEntity) (Object) this).getY(), entity2.getZ() - ((FishingBobberEntity) (Object) this).getZ())).multiply(0.1D);
            entity.setVelocity(entity.getVelocity().add(vec3d));
        }
    }

    public boolean removeIfInvalid(PlayerEntity player) {
        ItemStack itemStack = player.getMainHandStack();
        ItemStack itemStack2 = player.getOffHandStack();
        boolean bl = itemStack.isOf(Items.FISHING_ROD);
        boolean bl2 = itemStack2.isOf(Items.FISHING_ROD);
        if (!player.isRemoved() && player.isAlive() && (bl || bl2) && !(((FishingBobberEntity) (Object) this).squaredDistanceTo(player) > 1024.0D)) {
            return false;
        } else {
            ((FishingBobberEntity) (Object) this).discard();
            return true;
        }
    }
}
