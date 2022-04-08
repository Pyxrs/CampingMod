package io.github.simplycmd.camping.datagen;

import io.github.simplycmd.camping.Main;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;

import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;

import java.util.function.Consumer;

public class ModDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(new FabricRecipeProvider(fabricDataGenerator) {
            @Override
            protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
                offerPlanksRecipe(exporter, Blocks.OAK_PLANKS, Main.PINE_LOG);
                offerBarkBlockRecipe(exporter, Blocks.OAK_WOOD, Main.PINE_LOG);
                //offerBarkBlockRecipe(exporter, Blocks.STRIPPED_OAK_WOOD, Main.STRIPPED_PINE_LOG);
                //offerBoatRecipe(exporter, Items.OAK_BOAT, Blocks.OAK_PLANKS);

            }

            public static void offerPlanksRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input) {
                ShapelessRecipeJsonBuilder.create(output, 4).input(input).group("planks").criterion("has_logs", conditionsFromItem(input)).offerTo(exporter);
            }
        });
    }
}
