package io.github.simplycmd.camping;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class BiomeKeys {
    public static final RegistryKey<Biome> CAMPING_FOREST = key("camping_forest");
    public static final RegistryKey<Biome> DENSE_FOREST = key("dense_forest");

    static RegistryKey<Biome> key(String name) {
        Identifier id = new Identifier("camping", name);
        return RegistryKey.of(Registry.BIOME_KEY, id);
    }
}
