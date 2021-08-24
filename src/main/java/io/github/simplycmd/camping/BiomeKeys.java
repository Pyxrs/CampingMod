package io.github.simplycmd.camping;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BiomeKeys {
    // Identifiers for biomes
    public static final RegistryKey<Biome> PINE_FOREST = key("pine_forest");
    public static final RegistryKey<Biome> DENSE_PINE_FOREST = key("dense_pine_forest");

    static RegistryKey<Biome> key(String name) {
        Identifier id = new Identifier(Main.MOD_ID, name);
        return RegistryKey.of(Registry.BIOME_KEY, id);
    }
}
