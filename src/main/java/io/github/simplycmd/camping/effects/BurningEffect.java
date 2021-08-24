package io.github.simplycmd.camping.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class BurningEffect extends StatusEffect{

    private static final float damage = 0;

    public BurningEffect() {
        super(
            StatusEffectType.NEUTRAL,
            0x98D982);
        
    }
    
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.setOnFire(true);
        super.applyUpdateEffect(entity, amplifier);
    }
}
