package io.github.simplycmd.camping;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;


import static io.github.simplycmd.camping.Main.MOD_ID;

import net.minecraft.util.registry.BuiltinRegistries;

public class PineForest {
    // Biomes using Terraform API // NOT ANYMORE HAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHA
//    private static final BiomeTemplate PINE_FOREST_TEMPLATE = new BiomeTemplate(TerraformBiomeBuilder.create()
//            .configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG)
//            .addDefaultFeatures(LAND_CARVERS, DEFAULT_UNDERGROUND_STRUCTURES, DUNGEONS, MINEABLES, ORES, DISKS, DEFAULT_MUSHROOMS, DEFAULT_VEGETATION, FROZEN_TOP_LAYER, FOREST_FLOWERS, FOREST_GRASS, LAKES, SPRINGS) // Add multiple times to decrease rarity
//            .addStructureFeatures(ConfiguredStructureFeatures.STRONGHOLD, ConfiguredStructureFeatures.MINESHAFT, ConfiguredStructureFeatures.PILLAGER_OUTPOST, ConfiguredStructureFeatures.RUINED_PORTAL)
//            .precipitation(Biome.Precipitation.RAIN)
//            .addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ConfiguredFeatures.PATCH_LARGE_FERN)
//            .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Main.PINE_TREES)
//            .addFeature(GenerationStep.Feature.LAKES, Main.HOT_SPRINGS)
//            .category(Biome.Category.FOREST)
//            .addDefaultSpawnEntries()
//            .addSpawnEntry(new SpawnSettings.SpawnEntry(Main.BROWN_BEAR, 100, 1, 1))
//            .effects(new BiomeEffects.Builder()
//                    .waterColor(0x3F76E4)
//                    .waterFogColor(0x50533)
//                    .skyColor(0x77adff)
//                    .fogColor(0xC0D8FF)
//                    .grassColor(0x338235)
//                    .foliageColor(0x338235)
//            )
//            .temperature(0.6F)
//            .downfall(0.9F)
//    );

    private static Biome createDensePineForest() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(Main.BROWN_BEAR, 100, 1, 1));
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        DefaultBiomeFeatures.addDefaultOres(generationSettings);
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, PineTree.PINE_TREE_VEGITAL_FEATURE_PLACED);
        generationSettings.feature(GenerationStep.Feature.LAKES, BuiltinRegistries.PLACED_FEATURE.getEntry(BuiltinRegistries.PLACED_FEATURE.getKey(BuiltinRegistries.PLACED_FEATURE.get(Identifier.tryParse(MOD_ID + ":hot_springs"))).get()).get());
        generationSettings.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, VegetationPlacedFeatures.PATCH_LARGE_FERN);
        return (new Biome.Builder())
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.FOREST)
                .temperature(0.8F)
                .downfall(0.4F)
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x050533)
                        .fogColor(0xc0d8ff)
                        .skyColor(0x77adff)
                        .grassColor(0x558233)
                        .foliageColor(0x658233)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }
    private static Biome createPineForest() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(Main.BROWN_BEAR, 100, 1, 1));
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        DefaultBiomeFeatures.addDefaultOres(generationSettings);
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, BuiltinRegistries.PLACED_FEATURE.getEntry(BuiltinRegistries.PLACED_FEATURE.getKey(BuiltinRegistries.PLACED_FEATURE.get(Identifier.tryParse(MOD_ID + ":hot_springs"))).get()).get());
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, PineTree.PINE_TREE_VEGITAL_FEATURE_PLACED);
        generationSettings.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, VegetationPlacedFeatures.PATCH_LARGE_FERN);

        return (new Biome.Builder())
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.FOREST)
                .temperature(0.8F)
                .downfall(0.4F)
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x050533)
                        .fogColor(0xc0d8ff)
                        .skyColor(0x77adff)
                        .grassColor(0x558233)
                        .foliageColor(0x658233)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }
    static final Biome PINE_FOREST = createPineForest();
//    static final Biome OLD_PINE_FOREST = PINE_FOREST_TEMPLATE.builder()
//            .depth(0.4F)
//            .scale(0.4F)
//            .playerSpawnFriendly()
//            .build();

//    private static final BiomeTemplate DENSE_PINE_FOREST_TEMPLATE = new BiomeTemplate(TerraformBiomeBuilder.create()
//            .configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.PODZOL_CONFIG)
//            .addDefaultFeatures(LAND_CARVERS, DEFAULT_UNDERGROUND_STRUCTURES, DUNGEONS, MINEABLES, ORES, DISKS, DEFAULT_MUSHROOMS, DEFAULT_VEGETATION, FROZEN_TOP_LAYER, FOREST_FLOWERS, FOREST_GRASS, LAKES, SPRINGS) // Add multiple times to decrease rarity
//            .addStructureFeatures(ConfiguredStructureFeatures.STRONGHOLD, ConfiguredStructureFeatures.MINESHAFT, ConfiguredStructureFeatures.PILLAGER_OUTPOST, ConfiguredStructureFeatures.RUINED_PORTAL)
//            .precipitation(Biome.Precipitation.RAIN)
//            .addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ConfiguredFeatures.PATCH_LARGE_FERN)
//            .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Main.PINE_TREES)
//            .addFeature(GenerationStep.Feature.LAKES, Main.HOT_SPRINGS)
//            .category(Biome.Category.FOREST)
//            .addDefaultSpawnEntries()
//            .addSpawnEntry(new SpawnSettings.SpawnEntry(Main.BROWN_BEAR, 100, 1, 1))
//            .effects(new BiomeEffects.Builder()
//                    .waterColor(0x3F76E4)
//                    .waterFogColor(0x50533)
//                    .skyColor(0x77adff)
//                    .fogColor(0xC0D8FF)
//                    .grassColor(0x558233)
//                    .foliageColor(0x658233)
//            )
//            .temperature(0.6F)
//            .downfall(0.9F)
//    );

    static final Biome DENSE_PINE_FOREST = createDensePineForest();
//    static final Biome OLD_DENSE_PINE_FOREST = DENSE_PINE_FOREST_TEMPLATE.builder()
//            .depth(0.4F)
//            .scale(0.6F)
//            .playerSpawnFriendly()
//            .build();
}