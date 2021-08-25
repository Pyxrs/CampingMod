package io.github.simplycmd.camping.items;

import io.github.simplycmd.camping.Main;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MarshmallowOnStickItem extends Item {
    private static final double CAMPFIRE_DISTANCE = 1.5;
    private final Cooked cooked;

    public MarshmallowOnStickItem(Cooked cooked) {
        // Returns the correct food value based on the enum selected using "yield" and a switch case
        super(switch (cooked) {
            case RAW:
                yield new FabricItemSettings().group(ItemGroup.FOOD).maxCount(1).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.5f).snack().alwaysEdible().build());
            case WARM:
                yield new FabricItemSettings().group(ItemGroup.FOOD).maxCount(1).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.75f).snack().alwaysEdible().build());
            case GOLDEN:
                yield new FabricItemSettings().group(ItemGroup.FOOD).maxCount(1).food(new FoodComponent.Builder().hunger(4).saturationModifier(1.25f).snack().alwaysEdible().build());
            case HALFBURNT:
                yield new FabricItemSettings().group(ItemGroup.FOOD).maxCount(1).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.5f).snack().alwaysEdible().build());
            case BURNT:
                yield new FabricItemSettings().group(ItemGroup.FOOD).maxCount(1).food(new FoodComponent.Builder().hunger(1).saturationModifier(0f).snack().alwaysEdible().build());
            case FLAMING:
                yield new FabricItemSettings().group(ItemGroup.FOOD).maxCount(1);
            default:
                throw new EnumConstantNotPresentException(Cooked.class, "cooked");
        });
        this.cooked = cooked;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        LivingEntity livingEntity = (LivingEntity) entity;

        // Check if in hand
        if (livingEntity.getMainHandStack().getItem().equals(this) || livingEntity.getOffHandStack().getItem().equals(this)) {
            BlockHitResult hit = raycast(world, livingEntity, RaycastContext.FluidHandling.NONE);
            // Expensive distance call will hopefully be called rarely enough to not impact performance
            if (hit.squaredDistanceTo(livingEntity) <= CAMPFIRE_DISTANCE) cook(livingEntity, stack, livingEntity.getMainHandStack().getItem().equals(this) ? Hand.MAIN_HAND : Hand.OFF_HAND);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);

        if (stack.isEmpty()) {
            return new ItemStack(Items.STICK);
        } else {
            if (user instanceof PlayerEntity playerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
                ItemStack itemStack = new ItemStack(Items.STICK);
                if (!playerEntity.getInventory().insertStack(itemStack)) {
                    playerEntity.dropItem(itemStack, false);
                }
            }
            return stack;
        }
    }

    // Cooking progress NBT data
    public static Optional<Integer> getCooked(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null)
            return Optional.of(nbtCompound.getInt("Cooked"));
        else
            return Optional.empty();
    }

    public static void setCooked(ItemStack stack, int cooked) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt("Cooked", cooked);
    }

    // Responsible for cooking the marshmallow
    private void cook(LivingEntity entity, ItemStack stack, Hand hand) {
        if (getCooked(stack).isEmpty()) {
            setCooked(stack, 0);
        } else {
            // Increase cooked
            setCooked(stack, getCooked(stack).get() + 1);

            if (getCooked(stack).get() >= cooked.getBurnTime()) {
                Optional<Item> next = cooked.getNext();
                // Increase cooked level
                if (next.isPresent())
                    entity.setStackInHand(hand, next.get().getDefaultStack());
                else
                    entity.setStackInHand(hand, Items.STICK.getDefaultStack());
            }
        }
    }

    // Raycast method yoinked from Minecraft code
    private static BlockHitResult raycast(World world, LivingEntity entity, RaycastContext.FluidHandling fluidHandling) {
        float f = entity.getPitch();
        float g = entity.getYaw();
        Vec3d vec3d = entity.getEyePos();
        float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
        float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
        float j = -MathHelper.cos(-f * 0.017453292F);
        float k = MathHelper.sin(-f * 0.017453292F);
        float l = i * j;
        float n = h * j;
        Vec3d vec3d2 = vec3d.add((double)l * 5.0D, (double)k * 5.0D, (double)n * 5.0D);
        return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, fluidHandling, entity));
    }

    public enum Cooked {
        BURNT(50, Optional.empty()),
        FLAMING(5, Optional.empty()),
        HALFBURNT(10, Optional.empty()),
        GOLDEN(25, Optional.empty()),
        WARM(50, Optional.empty()),
        RAW(100, Optional.empty());

        private Optional<Item> nextItem;
        private final int burnTime;

        private Optional<Item> getNext() {
            return nextItem;
        }

        public static void updateItems() {
            FLAMING.nextItem = Optional.of(Main.MARSHMALLOW_ON_STICK_BURNT);
            HALFBURNT.nextItem = Optional.of(Main.MARSHMALLOW_ON_STICK_FLAMING);
            GOLDEN.nextItem = Optional.of(Main.MARSHMALLOW_ON_STICK_HALFBURNT);
            WARM.nextItem = Optional.of(Main.MARSHMALLOW_ON_STICK_GOLDEN);
            RAW.nextItem = Optional.of(Main.MARSHMALLOW_ON_STICK_WARM);
        }

        private int getBurnTime() {
            return burnTime;
        }

        Cooked(int burnTime, Optional<Item> nextItem) {
            this.nextItem = nextItem;
            this.burnTime = burnTime;
        }
    }
}