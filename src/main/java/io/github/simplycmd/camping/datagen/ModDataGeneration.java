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
                offerBarkBlockRecipe(exporter, Blocks.OAK_WOOD, Main.PINE_LOG);
                offerBoatRecipe(exporter, Items.OAK_BOAT, Main.PINE_PLANKS);
                offerBarkBlockRecipe(exporter, Blocks.STRIPPED_OAK_WOOD, Main.STRIPPED_PINE_LOG);
                createSignRecipe(Main.PINE_SIGN, Ingredient.ofItems(Main.PINE_PLANKS)).criterion(hasItem(Main.PINE_PLANKS), conditionsFromItem(Main.PINE_PLANKS)).offerTo(exporter);
                //offerBoatRecipe(exporter, Items.OAK_BOAT, Blocks.OAK_PLANKS);

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
                getOrCreateTagBuilder(Main.PINE_LOGS).add(Main.PINE_LOG.asItem(), Main.STRIPPED_PINE_LOG.asItem());
                getOrCreateTagBuilder(ItemTags.PLANKS).add(Main.PINE_PLANKS.asItem());
                getOrCreateTagBuilder(ItemTags.SIGNS).add(Main.PINE_SIGN.asItem());
            }
        });

        fabricDataGenerator.addProvider(new FabricTagProvider.BlockTagProvider(fabricDataGenerator) {
            @Override
            protected void generateTags() {
                getOrCreateTagBuilder(BlockTags.LOGS).addTag(Main.PINE_LOGS_BLOCKS);
                getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).addTag(Main.PINE_LOGS_BLOCKS);
                getOrCreateTagBuilder(Main.PINE_LOGS_BLOCKS).add(Main.PINE_LOG, Main.STRIPPED_PINE_LOG);
                getOrCreateTagBuilder(BlockTags.PLANKS).add(Main.PINE_PLANKS);
                getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(Main.PINE_SIGN);
                getOrCreateTagBuilder(BlockTags.SIGNS).add(Main.PINE_SIGN, Main.PINE_WALL_SIGN);
                getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(Main.PINE_WALL_SIGN);
                getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(Main.PINE_SIGN, Main.PINE_WALL_SIGN);
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
            }
        });

        fabricDataGenerator.addProvider(new FabricModelProvider(fabricDataGenerator) {
            @Override
            public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
                //blockStateModelGenerator.registerCubeAllModelTexturePool(Main.PINE_PLANKS);
                blockStateModelGenerator.registerParentedItemModel(Main.PINE_PLANKS, new Identifier("camping", "block/pine_planks"));
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
