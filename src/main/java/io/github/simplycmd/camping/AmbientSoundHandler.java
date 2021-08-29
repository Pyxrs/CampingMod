package io.github.simplycmd.camping;

import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AmbientSoundHandler {
    private static final Random RANDOM = new Random();
    private static final float SOUND_CHANCE = 0.005F;

    enum Sounds {
        WINDY1(Main.WINDY1_EVENT, 5),
        WINDY2(Main.WINDY2_EVENT, 5);

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
        ClientTickCallback.EVENT.register((client) -> {
            if (client.world != null && RANDOM.nextFloat() < SOUND_CHANCE) {
                Sounds sound = Sounds.random();
                client.world.playSound(null, client.player.getBlockPos().up(sound.yOffset).add(RANDOM.nextInt(6) - 3, 0, RANDOM.nextInt(6) - 3), sound.event, SoundCategory.BLOCKS, 1f, 1f);
                System.out.println("sound");
            }
        });
    }
}
