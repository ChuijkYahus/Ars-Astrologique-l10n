package com.iridiumposting.arsastrologique.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectStalwart extends MobEffect {

    public EffectStalwart(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return super.shouldApplyEffectTickThisTick(duration, amplifier);
    }

    @Override
    public boolean isBeneficial() {
        return super.isBeneficial();
    }
}
