package io.github.simplycmd.camping;

import io.github.simplycmd.camping.marshmallows.MarshmallowOnStick;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class Main implements ModInitializer {
	public static final TreeFeatureConfig CAMPING_TREE_CONFIG = new TreeFeatureConfig.Builder(
			new SimpleBlockStateProvider(Blocks.SPRUCE_LOG.getDefaultState()),
			new StraightTrunkPlacer(12, 12, 4),
			new SimpleBlockStateProvider(Blocks.DARK_OAK_LEAVES.getDefaultState()),
			new SimpleBlockStateProvider(Blocks.SPRUCE_SAPLING.getDefaultState()),
			new SpruceFoliagePlacer(UniformIntProvider.create(2, 3), UniformIntProvider.create(1, 1), UniformIntProvider.create(4, 12)),
			new TwoLayersFeatureSize(2, 0, 4)
	).ignoreVines().build();
	public static final ConfiguredFeature<?, ?> CAMPING_TREES = Feature.TREE.configure(CAMPING_TREE_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(100, 1, 2)));
	public static final Item MARSHMALLOW = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.5f).snack().build()));
	public static final Item MARSHMALLOW_ON_STICK_RAW = new MarshmallowOnStick(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.5f).snack().alwaysEdible().build()));
	public static final Item MARSHMALLOW_ON_STICK_WARM = new MarshmallowOnStick(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.75f).snack().alwaysEdible().build()));
	public static final Item MARSHMALLOW_ON_STICK_GOLDEN = new MarshmallowOnStick(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(3).saturationModifier(1.25f).snack().alwaysEdible().build()));
	public static final Item MARSHMALLOW_ON_STICK_HALFBURNT = new MarshmallowOnStick(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.5f).snack().alwaysEdible().build()));
	public static final Item MARSHMALLOW_ON_STICK_BURNT = new MarshmallowOnStick(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0f).snack().alwaysEdible().meat().build()));



	@Override
	public void onInitialize() {
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("camping", "camping_trees"), CAMPING_TREES);

		Registry.register(BuiltinRegistries.BIOME, BiomeKeys.CAMPING_FOREST.getValue(), CampingForest.CAMPING_FOREST);
		OverworldBiomes.addContinentalBiome(BiomeKeys.CAMPING_FOREST, OverworldClimate.SNOWY, 100D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.CAMPING_FOREST, OverworldClimate.TEMPERATE, 100D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.CAMPING_FOREST, OverworldClimate.COOL, 100D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.CAMPING_FOREST, OverworldClimate.DRY, 100D);

		Registry.register(BuiltinRegistries.BIOME, BiomeKeys.DENSE_FOREST.getValue(), CampingForest.DENSE_FOREST);
		OverworldBiomes.addContinentalBiome(BiomeKeys.DENSE_FOREST, OverworldClimate.SNOWY, 60D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.DENSE_FOREST, OverworldClimate.TEMPERATE, 60D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.DENSE_FOREST, OverworldClimate.COOL, 60D);
		Registry.register(Registry.ITEM, new Identifier("camping", "marshmallow"), MARSHMALLOW);
		Registry.register(Registry.ITEM, new Identifier("camping", "raw_marshmallow_on_stick"), MARSHMALLOW_ON_STICK_RAW);
		Registry.register(Registry.ITEM, new Identifier("camping", "warm_marshmallow_on_stick"), MARSHMALLOW_ON_STICK_WARM);
		Registry.register(Registry.ITEM, new Identifier("camping", "golden_marshmallow_on_stick"), MARSHMALLOW_ON_STICK_GOLDEN);
		Registry.register(Registry.ITEM, new Identifier("camping", "half_burnt_marshmallow_on_stick"), MARSHMALLOW_ON_STICK_HALFBURNT);
		Registry.register(Registry.ITEM, new Identifier("camping", "burnt_marshmallow_on_stick"), MARSHMALLOW_ON_STICK_BURNT);

	}
}
