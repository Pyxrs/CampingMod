package io.github.simplycmd.camping.effects;

import io.github.simplycmd.camping.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;

public class CozinessEffect extends StatusEffect{
    public CozinessEffect() {
        super(
            StatusEffectCategory.NEUTRAL,
            0xcfa136);
        
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (this == Main.COZINESS) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(1.0F);
            }
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        if (this == Main.COZINESS) {
            i = 50 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        }
        return super.canApplyUpdateEffect(duration, amplifier);
    }
}
