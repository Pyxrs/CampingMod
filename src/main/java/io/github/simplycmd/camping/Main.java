package io.github.simplycmd.camping;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.simplycmd.camping.blocks.HotSpringWaterBlock;
import io.github.simplycmd.camping.blocks.PineLogBlock;
import io.github.simplycmd.camping.blocks.SleepingBagBlock;
import io.github.simplycmd.camping.effects.CozinessEffect;
import io.github.simplycmd.camping.entities.bass.BassEntity;
import io.github.simplycmd.camping.entities.bass.BassEntityModel;
import io.github.simplycmd.camping.entities.bass.BassEntityRenderer;
import io.github.simplycmd.camping.entities.bear.BrownBearEntity;
import io.github.simplycmd.camping.entities.bear.BrownBearEntityModel;
import io.github.simplycmd.camping.entities.bear.BrownBearEntityRenderer;
import io.github.simplycmd.camping.items.FlamingFoodItem;
import io.github.simplycmd.camping.items.MarshmallowOnStickItem;
import io.github.simplycmd.camping.items.SleepingBagBlockItem;
import io.github.simplycmd.camping.items.TentItem;
import io.github.simplycmd.camping.mixin.AxeItemAccessor;
import io.github.simplycmd.camping.mixin.SignTypeAccessor;
import io.github.simplycmd.camping.mixin.SimpleBlockStateProviderAccessor;
import io.github.simplycmd.camping.mixin.TreeConfiguredFeaturesAccessor;
import net.fabricmc.api.*;
//import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
//import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
//import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
//import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.treedecorator.LeavesVineTreeDecorator;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;

import static io.github.simplycmd.camping.blocks.PineLogBlock.ACTIVE;
import static net.minecraft.block.Blocks.DARK_OAK_LEAVES;
import static net.minecraft.block.Blocks.DIRT;
import static net.minecraft.block.PillarBlock.AXIS;
import static net.minecraft.client.render.TexturedRenderLayers.SIGNS_ATLAS_TEXTURE;
import static net.minecraft.client.render.TexturedRenderLayers.WOOD_TYPE_TEXTURES;
import static net.minecraft.data.family.BlockFamilies.register;
import static net.minecraft.world.gen.feature.VegetationPlacedFeatures.*;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ClientModInitializer.class)
public class Main implements ModInitializer, ClientModInitializer, TerraBlenderApi {

	// Mod ID
	public static final String MOD_ID = "camping";

	public static final SignType PINE_SIGN_TYPE = SignTypeAccessor.createSignType("camping_pine");
	public static final TagKey<Item> PINE_LOGS = TagKey.of(Registry.ITEM.getKey(), new Identifier(MOD_ID, "pine_logs"));
	public static final TagKey<Block> PINE_LOGS_BLOCKS = TagKey.of(Registry.BLOCK.getKey(), new Identifier(MOD_ID, "pine_logs"));
	// Entities
	public static final EntityType<BrownBearEntity> BROWN_BEAR = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(MOD_ID, "brown_bear"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BrownBearEntity::new).dimensions(EntityDimensions.fixed(1.4F, 1.4F)).trackRangeBlocks(10).build()
	);
	public static final EntityType<BassEntity> BASS = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(MOD_ID, "bass"),
			FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, BassEntity::new).dimensions(EntityDimensions.fixed(0.7F, 0.5F)).trackRangeBlocks(4).build()
	);
	
	// Blocks
	public static final Block PINE_LOG = new PineLogBlock(FabricBlockSettings.of(Material.WOOD, MapColor.BROWN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD).ticksRandomly());
	public static final Block PINE_PLANKS = new Block(AbstractBlock.Settings.of(Material.WOOD, MapColor.PALE_YELLOW).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Block PINE_SLAB = new SlabBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.PALE_YELLOW).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Block PINE_STAIRS = new StairsBlock(PINE_PLANKS.getDefaultState(), AbstractBlock.Settings.copy(PINE_PLANKS));
	public static final Block STRIPPED_PINE_LOG = createLogBlock(MapColor.PALE_YELLOW, MapColor.PALE_YELLOW);
	public static final Block HOT_SPRING_WATER = new HotSpringWaterBlock(FabricBlockSettings.of(Material.BUBBLE_COLUMN).noCollision().dropsNothing());
	public static final Block SLEEPING_BAG = new SleepingBagBlock(FabricBlockSettings.of(Material.WOOL).noCollision());
	public static final Block PINE_SIGN = new SignBlock(AbstractBlock.Settings.of(Material.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD), Main.PINE_SIGN_TYPE);
	public static final Block PINE_WALL_SIGN = new WallSignBlock(AbstractBlock.Settings.of(Material.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD), Main.PINE_SIGN_TYPE);
	public static final Block PINE_FENCE = new FenceBlock(AbstractBlock.Settings.copy(PINE_PLANKS));
	public static final Block PINE_FENCE_GATE = new FenceGateBlock(AbstractBlock.Settings.copy(PINE_PLANKS));
	public static final Block PINE_WOOD = new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.BROWN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Block STRIPPED_PINE_WOOD = new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.BROWN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Block PINE_PRESSURE_PLATE = new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, AbstractBlock.Settings.of(Material.WOOD, PINE_PLANKS.getDefaultMapColor()).noCollision().strength(0.5F).sounds(BlockSoundGroup.WOOD));

	// Items
	public static final Item SAP = new Item(new FabricItemSettings().group(ItemGroup.MATERIALS));
	public static final Item CLOTH = new Item(new FabricItemSettings().group(ItemGroup.MATERIALS));
	public static final Item MARSHMALLOW = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.5f).snack().build()));
	public static final Item TENT = new TentItem(new FabricItemSettings().group(ItemGroup.MATERIALS));
	public static final Item BASS_BUCKET = new EntityBucketItem(BASS, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, (new Item.Settings()).maxCount(1).group(ItemGroup.MISC));
	public static final Item RAW_BASS = new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(3).saturationModifier(0.7f).build()));
	public static final Item COOKED_BASS = new Item(new FabricItemSettings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(6).saturationModifier(6.0f).build()));

	public static final SwordItem BEAR_CLAW = new SwordItem(ToolMaterials.IRON, 3, -2.0F, new FabricItemSettings().group(ItemGroup.COMBAT));

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

	// Effects
	public static final StatusEffect COZINESS = new CozinessEffect();

	// Stats
	public static final Identifier BURNED = new Identifier(MOD_ID, "burnt_times");

	// Features
	public static final TreeFeatureConfig PINE_TREE_CONFIG = new TreeFeatureConfig.Builder(
			SimpleBlockStateProviderAccessor.create(PINE_LOG.getDefaultState().with(AXIS, Direction.Axis.Y).with(ACTIVE, true)),
			new StraightTrunkPlacer(12, 12, 4),
			SimpleBlockStateProviderAccessor.create(DARK_OAK_LEAVES.getDefaultState()),
		//	SimpleBlockStateProviderAccessor.create(Blocks.SPRUCE_SAPLING.getDefaultState()),
			new SpruceFoliagePlacer(UniformIntProvider.create(2, 3), UniformIntProvider.create(1, 1), UniformIntProvider.create(4, 12)),
			new TwoLayersFeatureSize(2, 0, 4)
	).ignoreVines().dirtProvider(BlockStateProvider.of(DIRT)).build();

//	public static final TreeFeatureConfig PINE_TREE_CONFIG = TreeConfiguredFeaturesAccessor.builder(
//			PINE_LOG,
//			DARK_OAK_LEAVES,
//			12,
//			12,
//			4,
//			3)
//			.decorators(ImmutableList.of(LeavesVineTreeDecorator.INSTANCE)).ignoreVines().build();

	public static final LakeFeature.Config LAKE_CONFIG = new LakeFeature.Config(BlockStateProvider.of(HOT_SPRING_WATER), BlockStateProvider.of(DIRT));
	//public static final ConfiguredFeature<?, ?> PINE_TREES = Feature.TREE.generate(PINE_TREE_CONFIG).decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP).decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(100, 1, 2)));
	public static final ConfiguredFeature<?, ?> HOT_SPRINGS = new ConfiguredFeature<>(Feature.LAKE, LAKE_CONFIG);/*Feature.LAKE.configure(new SingleStateFeatureConfig(HOT_SPRING_WATER.getDefaultState())).range(ConfiguredFeatures.Decorators.BOTTOM_TO_TOP).spreadHorizontally().applyChance(4)*/
	// Spawn eggs
	public static final Item BROWN_BEAR_SPAWN_EGG = new SpawnEggItem(BROWN_BEAR, 5059399, 2302766, new Item.Settings().group(ItemGroup.MISC));
	public static final Item BASS_SPAWN_EGG = new SpawnEggItem(BASS, 8081500, 10197120, new Item.Settings().group(ItemGroup.MISC));

	// Sounds
	public static final Identifier WINDY1_ID = new Identifier(MOD_ID + ":windy1");
	public static SoundEvent WINDY1_EVENT = new SoundEvent(WINDY1_ID);
	public static final Identifier WINDY2_ID = new Identifier(MOD_ID + ":windy2");
	public static SoundEvent WINDY2_EVENT = new SoundEvent(WINDY2_ID);
	public static final Identifier BIRDS1_ID = new Identifier(MOD_ID + ":birds1");
	public static SoundEvent BIRDS1_EVENT = new SoundEvent(BIRDS1_ID);
	public static final Identifier BIRDS2_ID = new Identifier(MOD_ID + ":birds2");
	public static SoundEvent BIRDS2_EVENT = new SoundEvent(BIRDS2_ID);

	private static PillarBlock createLogBlock(MapColor topMapColor, MapColor sideMapColor) {
		return new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (state) -> {
			return state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor;
		}).strength(2.0F,  3.0F).sounds(BlockSoundGroup.WOOD));
	}

	public static final BlockFamily PINE_TYPE = register(PINE_PLANKS)/*.button(Blocks.ACACIA_BUTTON)*/.fence(PINE_FENCE).fenceGate(PINE_FENCE_GATE).pressurePlate(PINE_PRESSURE_PLATE).sign(PINE_SIGN, PINE_WALL_SIGN).slab(PINE_SLAB).stairs(PINE_STAIRS)/*.door(Blocks.ACACIA_DOOR).trapdoor(Blocks.ACACIA_TRAPDOOR)*/.group("wooden").unlockCriterionName("has_planks").build();
	@Override
	public void onInitialize() {
		System.out.println(ModBoatVariants.PINE);
		System.out.println(Arrays.toString(BoatEntity.Type.values()));
		SignTypeAccessor.callRegister(PINE_SIGN_TYPE);
		PineTree.register();
		MarshmallowOnStickItem.Cooked.updateItems();

		// --------------------------------------------------------------------
		// Register Sounds
		Registry.register(Registry.SOUND_EVENT, WINDY1_ID, WINDY1_EVENT);
		Registry.register(Registry.SOUND_EVENT, WINDY2_ID, WINDY2_EVENT);
		Registry.register(Registry.SOUND_EVENT, BIRDS1_ID, BIRDS1_EVENT);
		Registry.register(Registry.SOUND_EVENT, BIRDS2_ID, BIRDS2_EVENT);

		// --------------------------------------------------------------------
		// Register Entities
		FabricDefaultAttributeRegistry.register(BROWN_BEAR, BrownBearEntity.createBrownBearAttributes());
		FabricDefaultAttributeRegistry.register(BASS, BassEntity.createBassAttributes());

		// --------------------------------------------------------------------
		// Register Blocks
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_log"), PINE_LOG);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "stripped_pine_log"), STRIPPED_PINE_LOG);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_sign"), PINE_SIGN);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_wall_sign"), PINE_WALL_SIGN);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_planks"), PINE_PLANKS);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_slab"), PINE_SLAB);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_stairs"), PINE_STAIRS);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_fence"), PINE_FENCE);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_fence_gate"), PINE_FENCE_GATE);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_pressure_plate"), PINE_PRESSURE_PLATE);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "pine_wood"), PINE_WOOD);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "stripped_pine_wood"), STRIPPED_PINE_WOOD);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "hot_spring_water"), HOT_SPRING_WATER);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "sleeping_bag"), SLEEPING_BAG);

		// --------------------------------------------------------------------
		// Register Items
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_log"), new BlockItem(PINE_LOG, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "stripped_pine_log"), new BlockItem(STRIPPED_PINE_LOG, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_planks"), new BlockItem(PINE_PLANKS, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_stairs"), new BlockItem(PINE_STAIRS, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_slab"), new BlockItem(PINE_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_fence"), new BlockItem(PINE_FENCE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_fence_gate"), new BlockItem(PINE_FENCE_GATE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_pressure_plate"), new BlockItem(PINE_PRESSURE_PLATE, new Item.Settings().group(ItemGroup.REDSTONE)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_sign"), new SignItem((new Item.Settings()).maxCount(16).group(ItemGroup.DECORATIONS), PINE_SIGN, PINE_WALL_SIGN));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pine_wood"), new BlockItem(PINE_WOOD, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "stripped_pine_wood"), new BlockItem(STRIPPED_PINE_WOOD, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		var w = new HashMap<Block, Block>(AxeItemAccessor.getSTRIPPED_BLOCKS());
		w.put(Main.PINE_LOG, Main.STRIPPED_PINE_LOG);
		w.put(Main.PINE_WOOD, Main.STRIPPED_PINE_WOOD);
		AxeItemAccessor.setSTRIPPED_BLOCKS(ImmutableMap.copyOf(w));

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "sap"), SAP);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cloth"), CLOTH);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "marshmallow"), MARSHMALLOW);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tent"), TENT);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bass_bucket"), BASS_BUCKET);

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "raw_bass"), RAW_BASS);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cooked_bass"), COOKED_BASS);

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "sleeping_bag"), new SleepingBagBlockItem(SLEEPING_BAG, new FabricItemSettings().group(ItemGroup.DECORATIONS).maxCount(1)));

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bear_claw"), BEAR_CLAW);

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

		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "brown_bear_spawn_egg"), BROWN_BEAR_SPAWN_EGG);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bass_spawn_egg"), BASS_SPAWN_EGG);
		// --------------------------------------------------------------------
		// Register Stats
		Registry.register(Registry.CUSTOM_STAT, "burnt_times", BURNED);
		Stats.CUSTOM.getOrCreateStat(BURNED, StatFormatter.DEFAULT);

		var q = new ArrayList<PlacementModifier>();
		q.addAll(Lists.newArrayList(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(Blocks.SPRUCE_SAPLING.getDefaultState(), BlockPos.ORIGIN))));
		//q.add(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.getDefaultState(), BlockPos.ORIGIN)));
		//q.addAll(modifiers(PlacedFeatures.createCountExtraModifier(100, 1F, 2)));
		var d = new ArrayList<PlacementModifier>();
		d.addAll(Lists.newArrayList(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(Blocks.SPRUCE_SAPLING.getDefaultState(), BlockPos.ORIGIN))));
		//d.add(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.getDefaultState(), BlockPos.ORIGIN)));
		//d.addAll(modifiers(PlacedFeatures.createCountExtraModifier(150, 1F, 2)));
		// --------------------------------------------------------------------
		// Register Features
		//Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MOD_ID, "pine_trees"), PINE_TREES);
		//PlacedFeatures.register(MOD_ID + ":pine_trees", BuiltinRegistries.CONFIGURED_FEATURE.getEntry(BuiltinRegistries.CONFIGURED_FEATURE.getKey(PINE_TREES).get()).get(), q);
		//PlacedFeatures.register(MOD_ID + ":dense_pine_trees", BuiltinRegistries.CONFIGURED_FEATURE.getEntry(BuiltinRegistries.CONFIGURED_FEATURE.getKey(PINE_TREES).get()).get(), d);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MOD_ID, "hot_springs"), HOT_SPRINGS);
		PlacedFeatures.register(MOD_ID + ":hot_springs", BuiltinRegistries.CONFIGURED_FEATURE.getEntry(BuiltinRegistries.CONFIGURED_FEATURE.getKey(HOT_SPRINGS).get()).get(), modifiers(1));
		// --------------------------------------------------------------------
		// Register Biomes (OverworldBiomes is deprecated because it's experimental)
		Registry.register(BuiltinRegistries.BIOME, BiomeKeys.PINE_FOREST.getValue(), PineForest.PINE_FOREST);

//		OverworldBiomes.addContinentalBiome(BiomeKeys.PINE_FOREST, OverworldClimate.TEMPERATE, 1D);
//		OverworldBiomes.addContinentalBiome(BiomeKeys.PINE_FOREST, OverworldClimate.COOL, 1D);
//		OverworldBiomes.addContinentalBiome(BiomeKeys.PINE_FOREST, OverworldClimate.DRY, 1D);

		Registry.register(BuiltinRegistries.BIOME, BiomeKeys.DENSE_PINE_FOREST.getValue(), PineForest.DENSE_PINE_FOREST);
//		OverworldBiomes.addContinentalBiome(BiomeKeys.DENSE_PINE_FOREST, OverworldClimate.TEMPERATE, 0.5D);
//		OverworldBiomes.addContinentalBiome(BiomeKeys.DENSE_PINE_FOREST, OverworldClimate.COOL, 0.5D);

		// --------------------------------------------------------------------
		// Register Effects
		Registry.register(Registry.STATUS_EFFECT, new Identifier(MOD_ID, "coziness"), COZINESS);
		System.out.println("Camping Init");
	}

	@Environment(EnvType.CLIENT)
	public static EntityModelLayer MODEL_BROWN_BEAR_LAYER;
	@Environment(EnvType.CLIENT)
	public static EntityModelLayer MODEL_BASS_LAYER;

	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
//		ClientSpriteRegistryCallback.event(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
//			registry.register(new Identifier("camping", "entity/signs/pine"));
//		}));
//		WOOD_TYPE_TEXTURES.put(PINE_SIGN_TYPE, TexturedRenderLayers.getSignTextureId(PINE_SIGN_TYPE));
		//WOOD_TYPE_TEXTURES.forEach((a,b)->{System.out.println(a.getName()+ ", " +b.toString());});
		//System.exit(-1);
		// --------------------------------------------------------------------
		// Start client-side sounds

		// DISABLED for now because it's kinda shitty
		//AmbientSoundHandler.start();

		// --------------------------------------------------------------------
		// Register client-side block colors for maps
		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> state.get(SleepingBagBlock.COLOR).getFireworkColor(), SLEEPING_BAG);

		// --------------------------------------------------------------------
		// Register renderers for entities
		MODEL_BROWN_BEAR_LAYER = new EntityModelLayer(new Identifier(Main.MOD_ID, "brown_bear"), "main");
		EntityRendererRegistry.INSTANCE.register(Main.BROWN_BEAR, BrownBearEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_BROWN_BEAR_LAYER, BrownBearEntityModel::getTexturedModelData);

		MODEL_BASS_LAYER = new EntityModelLayer(new Identifier(Main.MOD_ID, "bass"), "main");
		EntityRendererRegistry.INSTANCE.register(Main.BASS, BassEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_BASS_LAYER, BassEntityModel::getTexturedModelData);

		// --------------------------------------------------------------------
		// Register renderers for blocks
		BlockRenderLayerMap.INSTANCE.putBlock(Main.SLEEPING_BAG, RenderLayer.getCutout());
	}


	@Override
	public void onTerraBlenderInitialized() {
		TerraBlenderApi.super.onTerraBlenderInitialized();
		Regions.register(new BiomeProvider(new Identifier(MOD_ID, "modded_region"), RegionType.OVERWORLD, 1));

	}
	private static class BiomeProvider extends Region {

		public BiomeProvider(Identifier name, RegionType type, int weight) {
			super(name, type, weight);
		}

		@Override
		public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
			super.addBiomes(registry, mapper);
			this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
				builder.replaceBiome(net.minecraft.world.biome.BiomeKeys.FOREST, BiomeKeys.PINE_FOREST);
				builder.replaceBiome(net.minecraft.world.biome.BiomeKeys.DARK_FOREST, BiomeKeys.DENSE_PINE_FOREST);
			});
			//this.addBiome(mapper, BiomeKeys.PINE_FOREST);
		}
	}
}
