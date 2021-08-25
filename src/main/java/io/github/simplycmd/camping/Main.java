package io.github.simplycmd.camping;

import io.github.simplycmd.camping.blocks.HotSpringWaterBlock;
import io.github.simplycmd.camping.blocks.PineLogBlock;
import io.github.simplycmd.camping.effects.BurningEffect;
import io.github.simplycmd.camping.effects.CozinessEffect;
import io.github.simplycmd.camping.items.FlamingFoodItem;
import io.github.simplycmd.camping.items.MarshmallowOnStickItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class Main implements ModInitializer {
	// Mod ID
	public static final String MOD_ID = "camping";
	
	// Blocks
	public static final Block PINE_LOG = new PineLogBlock(FabricBlockSettings.of(Material.WOOD, MapColor.BROWN).strength(2.0F).sounds(BlockSoundGroup.WOOD).ticksRandomly());
	public static final Block HOT_SPRING_WATER = new HotSpringWaterBlock(FabricBlockSettings.of(Material.BUBBLE_COLUMN).noCollision().dropsNothing());

	// Items
	public static final Item SAP = new Item(new FabricItemSettings().group(ItemGroup.MATERIALS));

	public static final Item MARSHMALLOW = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.5f).snack().build()));
	public static final Item MARSHMALLOW_ON_STICK_RAW = new MarshmallowOnStickItem(MarshmallowOnStickItem.Cooked.RAW);
	public static final Item MARSHMALLOW_ON_STICK_WARM = new MarshmallowOnStickItem(MarshmallowOnStickItem.Cooked.WARM);
	public static final Item MARSHMALLOW_ON_STICK_GOLDEN = new MarshmallowOnStickItem(MarshmallowOnStickItem.Cooked.GOLDEN);
	public static final Item MARSHMALLOW_ON_STICK_HALFBURNT = new MarshmallowOnStickItem(MarshmallowOnStickItem.Cooked.HALFBURNT);
	public static final Item MARSHMALLOW_ON_STICK_FLAMING = new MarshmallowOnStickItem(MarshmallowOnStickItem.Cooked.FLAMING);
	public static final Item MARSHMALLOW_ON_STICK_BURNT = new MarshmallowOnStickItem(MarshmallowOnStickItem.Cooked.BURNT);
	public static final Item BREADED_SMORE_WARM = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(6).saturationModifier(3.0f).build()));
	public static final Item BREADED_SMORE_GOLDEN = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(8).saturationModifier(5.0f).build()));
	public static final Item BREADED_SMORE_HALF_BURNT = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(5).saturationModifier(1.5f).build()));
	public static final Item BREADED_SMORE_BURNT = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(2).saturationModifier(1.0f).build()));
	public static final Item BREADED_SMORE_FLAMING = new FlamingFoodItem(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(4).saturationModifier(3.0f).build()));
	public static final Item COOKIE_SMORE_WARM = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(6).saturationModifier(3.0f).build()));
	public static final Item COOKIE_SMORE_GOLDEN = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(8).saturationModifier(5.0f).build()));
	public static final Item COOKIE_SMORE_HALF_BURNT = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(5).saturationModifier(1.5f).build()));
	public static final Item COOKIE_SMORE_BURNT = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(2).saturationModifier(1.0f).build()));
	public static final Item COOKIE_SMORE_FLAMING = new FlamingFoodItem(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(4).saturationModifier(3.0f).build()));



	//effects
	public static final StatusEffect COZINESS = new CozinessEffect();
	public static final StatusEffect BURNING = new BurningEffect();

	// Stats
	public static final Identifier BURNED = new Identifier(MOD_ID, "burnt_times");
	// Features
	public static final TreeFeatureConfig PINE_TREE_CONFIG = new TreeFeatureConfig.Builder(
			new SimpleBlockStateProvider(PINE_LOG.getDefaultState()),
			new StraightTrunkPlacer(12, 12, 4),
			new SimpleBlockStateProvider(Blocks.DARK_OAK_LEAVES.getDefaultState()),
			new SimpleBlockStateProvider(Blocks.SPRUCE_SAPLING.getDefaultState()),
			new SpruceFoliagePlacer(UniformIntProvider.create(2, 3), UniformIntProvider.create(1, 1), UniformIntProvider.create(4, 12)),
			new TwoLayersFeatureSize(2, 0, 4)
	).ignoreVines().build();
	public static final ConfiguredFeature<?, ?> PINE_TREES = Feature.TREE.configure(PINE_TREE_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(100, 1, 2)));
	public static final ConfiguredFeature<?, ?> HOT_SPRINGS = Feature.LAKE.configure(new SingleStateFeatureConfig(HOT_SPRING_WATER.getDefaultState())).range(ConfiguredFeatures.Decorators.BOTTOM_TO_TOP).spreadHorizontally().applyChance(4);

	@Override
	public void onInitialize() {
		MarshmallowOnStickItem.Cooked.updateItems();

		// Register blocks
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_log"), PINE_LOG);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "hot_spring_water"), HOT_SPRING_WATER);

		// Register items
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "sap"), SAP);

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "marshmallow"), MARSHMALLOW);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "raw_marshmallow_on_a_stick"), MARSHMALLOW_ON_STICK_RAW);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "warm_marshmallow_on_a_stick"), MARSHMALLOW_ON_STICK_WARM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "golden_marshmallow_on_a_stick"), MARSHMALLOW_ON_STICK_GOLDEN);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "half_burnt_marshmallow_on_a_stick"), MARSHMALLOW_ON_STICK_HALFBURNT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "flaming_marshmallow_on_a_stick"), MARSHMALLOW_ON_STICK_FLAMING);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "burnt_marshmallow_on_a_stick"), MARSHMALLOW_ON_STICK_BURNT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "breaded_smore_warm"), BREADED_SMORE_WARM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "breaded_smore_golden"), BREADED_SMORE_GOLDEN);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "breaded_smore_half_burnt"), BREADED_SMORE_HALF_BURNT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "breaded_smore_burnt"), BREADED_SMORE_BURNT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "breaded_smore_flaming"), BREADED_SMORE_FLAMING);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cookie_smore_warm"), COOKIE_SMORE_WARM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cookie_smore_golden"), COOKIE_SMORE_GOLDEN);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cookie_smore_half_burnt"), COOKIE_SMORE_HALF_BURNT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cookie_smore_burnt"), COOKIE_SMORE_BURNT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cookie_smore_flaming"), COOKIE_SMORE_FLAMING);


		// Register Stats
		Registry.register(Registry.CUSTOM_STAT, "burnt_times", BURNED);
		Stats.CUSTOM.getOrCreateStat(BURNED, StatFormatter.DEFAULT);

		// Register features
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MOD_ID, "pine_trees"), PINE_TREES);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MOD_ID, "hot_springs"), HOT_SPRINGS);

		// Register biomes (OverworldBiomes is deprecated because it's experimental)
		Registry.register(BuiltinRegistries.BIOME, BiomeKeys.PINE_FOREST.getValue(), PineForest.PINE_FOREST);
		OverworldBiomes.addContinentalBiome(BiomeKeys.PINE_FOREST, OverworldClimate.SNOWY, 100D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.PINE_FOREST, OverworldClimate.TEMPERATE, 100D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.PINE_FOREST, OverworldClimate.COOL, 100D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.PINE_FOREST, OverworldClimate.DRY, 100D);

		Registry.register(BuiltinRegistries.BIOME, BiomeKeys.DENSE_PINE_FOREST.getValue(), PineForest.DENSE_PINE_FOREST);
		OverworldBiomes.addContinentalBiome(BiomeKeys.DENSE_PINE_FOREST, OverworldClimate.SNOWY, 60D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.DENSE_PINE_FOREST, OverworldClimate.TEMPERATE, 60D);
		OverworldBiomes.addContinentalBiome(BiomeKeys.DENSE_PINE_FOREST, OverworldClimate.COOL, 60D);

		// Register Effects
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, "coziness"), COZINESS);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, "burning"), BURNING);
	}
}
