package io.github.simplycmd.camping;

import com.terraformersmc.terraform.biomebuilder.BiomeTemplate;
import com.terraformersmc.terraform.biomebuilder.TerraformBiomeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

import static com.terraformersmc.terraform.biomebuilder.DefaultFeature.*;

public class PineForest {
    // Biomes using Terraform API
    private static final BiomeTemplate PINE_FOREST_TEMPLATE = new BiomeTemplate(TerraformBiomeBuilder.create()
            .configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_CONFIG)
            .addDefaultFeatures(LAND_CARVERS, DEFAULT_UNDERGROUND_STRUCTURES, DUNGEONS, MINEABLES, ORES, DISKS, DEFAULT_MUSHROOMS, DEFAULT_VEGETATION, FROZEN_TOP_LAYER, FOREST_FLOWERS, FOREST_GRASS, LAKES, SPRINGS) // Add multiple times to decrease rarity
            .addStructureFeatures(ConfiguredStructureFeatures.STRONGHOLD, ConfiguredStructureFeatures.MINESHAFT, ConfiguredStructureFeatures.PILLAGER_OUTPOST, ConfiguredStructureFeatures.RUINED_PORTAL)
            .precipitation(Biome.Precipitation.RAIN)
            .addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ConfiguredFeatures.PATCH_LARGE_FERN)
            .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Main.PINE_TREES)
            .addFeature(GenerationStep.Feature.LAKES, Main.HOT_SPRINGS)
            .category(Biome.Category.FOREST)
            .addDefaultSpawnEntries()
            .addSpawnEntry(new SpawnSettings.SpawnEntry(Main.BROWN_BEAR, 100, 1, 1))
            .effects(new BiomeEffects.Builder()
                    .waterColor(0x3F76E4)
                    .waterFogColor(0x50533)
                    .skyColor(0x77adff)
                    .fogColor(0xC0D8FF)
                    .grassColor(0x338235)
                    .foliageColor(0x338235)
            )
            .temperature(0.6F)
            .downfall(0.9F)
    );

    static final Biome PINE_FOREST = PINE_FOREST_TEMPLATE.builder()
            .depth(0.4F)
            .scale(0.4F)
            .playerSpawnFriendly()
            .build();

    private static final BiomeTemplate DENSE_PINE_FOREST_TEMPLATE = new BiomeTemplate(TerraformBiomeBuilder.create()
            .configureSurfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.PODZOL_CONFIG)
            .addDefaultFeatures(LAND_CARVERS, DEFAULT_UNDERGROUND_STRUCTURES, DUNGEONS, MINEABLES, ORES, DISKS, DEFAULT_MUSHROOMS, DEFAULT_VEGETATION, FROZEN_TOP_LAYER, FOREST_FLOWERS, FOREST_GRASS, LAKES, SPRINGS) // Add multiple times to decrease rarity
            .addStructureFeatures(ConfiguredStructureFeatures.STRONGHOLD, ConfiguredStructureFeatures.MINESHAFT, ConfiguredStructureFeatures.PILLAGER_OUTPOST, ConfiguredStructureFeatures.RUINED_PORTAL)
            .precipitation(Biome.Precipitation.RAIN)
            .addFeature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, ConfiguredFeatures.PATCH_LARGE_FERN)
            .addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Main.PINE_TREES)
            .addFeature(GenerationStep.Feature.LAKES, Main.HOT_SPRINGS)
            .category(Biome.Category.FOREST)
            .addDefaultSpawnEntries()
            .addSpawnEntry(new SpawnSettings.SpawnEntry(Main.BROWN_BEAR, 100, 1, 1))
            .effects(new BiomeEffects.Builder()
                    .waterColor(0x3F76E4)
                    .waterFogColor(0x50533)
                    .skyColor(0x77adff)
                    .fogColor(0xC0D8FF)
                    .grassColor(0x558233)
                    .foliageColor(0x658233)
            )
            .temperature(0.6F)
            .downfall(0.9F)
    );

    static final Biome DENSE_PINE_FOREST = DENSE_PINE_FOREST_TEMPLATE.builder()
            .depth(0.4F)
            .scale(0.4F)
            .playerSpawnFriendly()
            .build();
}