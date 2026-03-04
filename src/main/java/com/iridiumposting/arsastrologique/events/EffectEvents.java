package com.iridiumposting.arsastrologique.events;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.setup.registry.AstroEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = ArsAstrologique.MODID)
public class EffectEvents{

    @SubscribeEvent
    public static void effectBacklash(MobEffectEvent.Expired event){
        LivingEntity entity = event.getEntity();
        MobEffectInstance effect = event.getEffectInstance();

        if(effect != null && effect.getEffect().equals(AstroEffectRegistry.ALACRITY_EFFECT)) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, effect.getAmplifier()+2));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,120, effect.getAmplifier()+2));
        }
    }

    @SubscribeEvent
    public static void damageReduction(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        var damage = event.getContainer().getOriginalDamage();

        if(entity.hasEffect(AstroEffectRegistry.STALWART_EFFECT)) {
            float amp = entity.getEffect(AstroEffectRegistry.STALWART_EFFECT).getAmplifier();
            if (damage > 0.5 && amp > 0) damage *= 1.0F - 0.2F*(amp+1);
        }
        event.getContainer().setNewDamage(damage);
    }

    @SubscribeEvent
    public static void healingAmplification(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        var healing = event.getAmount();

        if(entity.hasEffect(AstroEffectRegistry.BLESS_EFFECT)) {
            float amp = entity.getEffect(AstroEffectRegistry.BLESS_EFFECT).getAmplifier();
            if (healing > 0.5 && amp > 0) healing *= (1.0F + 0.2F*amp);
        }
        event.setAmount(healing);
    }
}
