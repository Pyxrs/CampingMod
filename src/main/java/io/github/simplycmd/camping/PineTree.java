package io.github.simplycmd.camping;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;

import static io.github.simplycmd.camping.Main.MOD_ID;
import static io.github.simplycmd.camping.Main.PINE_TREE_CONFIG;
import static net.minecraft.world.gen.feature.VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER;

public class PineTree {
    public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> PINE_TREES = ConfiguredFeatures.register(MOD_ID + ":pine_tree", Feature.TREE, PINE_TREE_CONFIG);
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> PINE_TREE_VEGITAL_FEATURE_CONFIGURED = ConfiguredFeatures.register(MOD_ID + ":pine_tree_configured", Feature.RANDOM_SELECTOR, new RandomFeatureConfig(List.of(new RandomFeatureEntry(PlacedFeatures.createEntry(PINE_TREES), 1F)), PlacedFeatures.createEntry(PINE_TREES)));
    public static final RegistryEntry<PlacedFeature> PINE_TREE_VEGITAL_FEATURE_PLACED = PlacedFeatures.register(MOD_ID + ":pine_tree_vegital", PINE_TREE_VEGITAL_FEATURE_CONFIGURED, PlacedFeatures.createCountExtraModifier(100, 1F, 1), SquarePlacementModifier.of(), NOT_IN_SURFACE_WATER_MODIFIER, PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP, BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.getDefaultState(), BlockPos.ORIGIN)), BiomePlacementModifier.of());
    public static void register() {

    }
}
