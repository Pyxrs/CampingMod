package io.github.simplycmd.camping.datagen;

import com.google.common.collect.ImmutableMap;
import io.github.simplycmd.camping.Main;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.data.DataGenerator;

import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.TexturedModel;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.data.server.LootTableProvider;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(new FabricRecipeProvider(fabricDataGenerator) {
            @Override
            protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
                offerPlanksRecipe(exporter, Main.PINE_PLANKS, Main.PINE_LOGS);
                offerBarkBlockRecipe(exporter, Main.PINE_WOOD, Main.PINE_LOG);
                offerBoatRecipe(exporter, Items.OAK_BOAT, Main.PINE_PLANKS);
                offerBarkBlockRecipe(exporter, Main.STRIPPED_PINE_WOOD, Main.STRIPPED_PINE_LOG);
                offerSlabRecipe(exporter, Main.PINE_SLAB, Main.PINE_PLANKS);
                createSignRecipe(Main.PINE_SIGN, Ingredient.ofItems(Main.PINE_PLANKS)).criterion(hasItem(Main.PINE_PLANKS), conditionsFromItem(Main.PINE_PLANKS)).offerTo(exporter);
                createStairsRecipe(Main.PINE_STAIRS, Ingredient.ofItems(Main.PINE_PLANKS)).criterion(hasItem(Main.PINE_PLANKS), conditionsFromItem(Main.PINE_PLANKS)).offerTo(exporter);
                //offerBoatRecipe(exporter, Items.OAK_BOAT, Blocks.OAK_PLANKS);
                createFenceRecipe(Main.PINE_FENCE, Ingredient.ofItems(Main.PINE_PLANKS)).criterion(hasItem(Main.PINE_PLANKS), conditionsFromItem(Main.PINE_PLANKS)).offerTo(exporter);
                createFenceGateRecipe(Main.PINE_FENCE_GATE, Ingredient.ofItems(Main.PINE_PLANKS)).criterion(hasItem(Main.PINE_PLANKS), conditionsFromItem(Main.PINE_PLANKS)).offerTo(exporter);
                createPressurePlateRecipe(exporter, Main.PINE_PRESSURE_PLATE, Main.PINE_PLANKS);
            }

//            public static void offerPlanksRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input) {
//                ShapelessRecipeJsonBuilder.create(output, 4).input(input).group("planks").criterion("has_logs", conditionsFromItem(input)).offerTo(exporter);
//            }
        });

        fabricDataGenerator.addProvider(new FabricTagProvider.ItemTagProvider(fabricDataGenerator) {
            @Override
            protected void generateTags() {
                getOrCreateTagBuilder(ItemTags.LOGS).addTag(Main.PINE_LOGS);
                getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN).addTag(Main.PINE_LOGS);
                getOrCreateTagBuilder(Main.PINE_LOGS).add(Main.PINE_LOG.asItem(), Main.STRIPPED_PINE_LOG.asItem(), Main.PINE_WOOD.asItem(), Main.STRIPPED_PINE_WOOD.asItem());
                getOrCreateTagBuilder(ItemTags.PLANKS).add(Main.PINE_PLANKS.asItem());
                getOrCreateTagBuilder(ItemTags.SIGNS).add(Main.PINE_SIGN.asItem());
                getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(Main.PINE_SLAB.asItem());
                getOrCreateTagBuilder(ItemTags.SLABS).add(Main.PINE_SLAB.asItem());

                getOrCreateTagBuilder(ItemTags.STAIRS).add(Main.PINE_STAIRS.asItem());
                getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(Main.PINE_STAIRS.asItem());

                getOrCreateTagBuilder(ItemTags.FENCES).add(Main.PINE_FENCE.asItem());
                getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(Main.PINE_FENCE.asItem());

                getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(Main.PINE_PRESSURE_PLATE.asItem());
            }
        });

        fabricDataGenerator.addProvider(new FabricTagProvider.BlockTagProvider(fabricDataGenerator) {
            @Override
            protected void generateTags() {
                getOrCreateTagBuilder(BlockTags.LOGS).addTag(Main.PINE_LOGS_BLOCKS);
                getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).addTag(Main.PINE_LOGS_BLOCKS);
                getOrCreateTagBuilder(Main.PINE_LOGS_BLOCKS).add(Main.PINE_LOG, Main.STRIPPED_PINE_LOG, Main.PINE_WOOD, Main.STRIPPED_PINE_WOOD);
                getOrCreateTagBuilder(BlockTags.PLANKS).add(Main.PINE_PLANKS);
                getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(Main.PINE_SIGN);
                getOrCreateTagBuilder(BlockTags.SIGNS).add(Main.PINE_SIGN, Main.PINE_WALL_SIGN);
                getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(Main.PINE_WALL_SIGN);
                getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(Main.PINE_SIGN, Main.PINE_WALL_SIGN);

                getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(Main.PINE_SLAB);
                getOrCreateTagBuilder(BlockTags.SLABS).add(Main.PINE_SLAB);

                getOrCreateTagBuilder(BlockTags.STAIRS).add(Main.PINE_STAIRS);
                getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(Main.PINE_STAIRS);

                getOrCreateTagBuilder(BlockTags.FENCES).add(Main.PINE_FENCE);
                getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(Main.PINE_FENCE);

                getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(Main.PINE_FENCE_GATE);

                getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(Main.PINE_PRESSURE_PLATE);
            }
        });

        fabricDataGenerator.addProvider(new FabricBlockLootTableProvider(fabricDataGenerator) {

            @Override
            protected void generateBlockLootTables() {
                addDrop(Main.PINE_LOG);
                addDrop(Main.STRIPPED_PINE_LOG);
                addDrop(Main.HOT_SPRING_WATER, Items.AIR);
                addDrop(Main.PINE_PLANKS);
                addDrop(Main.SLEEPING_BAG, LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(2, 3)).with(ItemEntry.builder(Main.CLOTH)).conditionally(SurvivesExplosionLootCondition.builder())));
                addDrop(Main.PINE_SLAB, BlockLootTableGenerator::slabDrops);
                addDrop(Main.PINE_STAIRS);
                addDrop(Main.PINE_FENCE);
                addDrop(Main.PINE_FENCE_GATE);
                addDrop(Main.PINE_WOOD);
                addDrop(Main.STRIPPED_PINE_WOOD);
                addDrop(Main.PINE_PRESSURE_PLATE);
            }
        });

        fabricDataGenerator.addProvider(new FabricModelProvider(fabricDataGenerator) {
            @Override
            public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
                //blockStateModelGenerator.registerCubeAllModelTexturePool(Main.PINE_PLANKS);
                blockStateModelGenerator.registerLog(Main.PINE_LOG).wood(Main.PINE_WOOD);
                blockStateModelGenerator.registerLog(Main.STRIPPED_PINE_LOG).wood(Main.STRIPPED_PINE_WOOD);
                blockStateModelGenerator.registerParentedItemModel(Main.PINE_PLANKS, new Identifier("camping", "block/pine_planks"));
                blockStateModelGenerator.registerParentedItemModel(Main.PINE_WOOD, new Identifier("camping", "block/pine_wood"));
                blockStateModelGenerator.registerParentedItemModel(Main.STRIPPED_PINE_WOOD, new Identifier("camping", "block/stripped_pine_wood"));
                blockStateModelGenerator.registerParentedItemModel(Main.PINE_FENCE_GATE, new Identifier("camping", "block/pine_fence_gate"));
                blockStateModelGenerator.registerParentedItemModel(Main.PINE_PRESSURE_PLATE, new Identifier("camping", "block/pine_pressure_plate"));
                Map<BlockFamily.Variant, BiConsumer<BlockStateModelGenerator.BlockTexturePool, Block>> a = ImmutableMap.of(BlockFamily.Variant.SIGN, BlockStateModelGenerator.BlockTexturePool::sign);
                BlockFamilies.getFamilies().filter(BlockFamily::shouldGenerateModels).forEach((family) -> {
                    if (family == Main.PINE_TYPE)
                    blockStateModelGenerator.registerCubeAllModelTexturePool(family.getBaseBlock()).family(family);
                });
            }

            @Override
            public void generateItemModels(ItemModelGenerator itemModelGenerator) {

            }
        });
    }
}
