package io.github.simplycmd.camping;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.Random;

public class AmbientSoundHandler {
    private static final Random RANDOM = new Random();
    private static final float SOUND_CHANCE = 0.0005F;

    enum Sounds {
        WINDY1(Main.WINDY1_EVENT, 5),
        WINDY2(Main.WINDY2_EVENT, 5),
        BIRDS1(Main.BIRDS1_EVENT, 7),
        BIRDS2(Main.BIRDS2_EVENT, 7);

        private final SoundEvent event;
        private final int yOffset;
        private static Sounds random() {
            return Sounds.values()[RANDOM.nextInt(Sounds.values().length)];
        }
        Sounds(SoundEvent event, int yOffset) {
            this.event = event;
            this.yOffset = yOffset;
        }
    }

    public static void start() {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (client.world != null && client.player.getBlockPos().getY() >= 64 && RANDOM.nextFloat() < SOUND_CHANCE && client.world.getBiomeAccess().getBiome(client.player.getBlockPos()).value().getFoliageColor()> 0) { // Janky but working way to detect modded biomes
                Sounds sound = Sounds.random();
                client.world.playSound(client.player, client.player.getBlockPos().up(sound.yOffset).add(RANDOM.nextInt(10) - 5, 0, RANDOM.nextInt(10) - 5), sound.event, SoundCategory.BLOCKS, RANDOM.nextFloat() + 1, 0.9F + (RANDOM.nextFloat() / 5));
            }
        });
    }
}
