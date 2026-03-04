package com.iridiumposting.arsastrologique.setup.registry;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.common.effect.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AstroEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ArsAstrologique.MODID);

    public static final Holder<MobEffect> ALACRITY_EFFECT = MOB_EFFECTS.register("alacrity",
            () -> new EffectAlacrity(MobEffectCategory.BENEFICIAL, 0xd3af37)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "alacrity"), 1.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "alacrity"), 1.5F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "alacrity"), 1.5F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MOVEMENT_EFFICIENCY, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "alacrity"), 2.0F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final Holder<MobEffect> STALWART_EFFECT = MOB_EFFECTS.register("stalwart",
            () -> new EffectStalwart(MobEffectCategory.BENEFICIAL, 0x6d8196)
                    .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "stalwart"), 1.0F, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "stalwart"), 1.0F, AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "stalwart"), -0.19F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "stalwart"), -0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final Holder<MobEffect> EMPOWER_EFFECT = MOB_EFFECTS.register("empower",
            () -> new EffectEmpower(MobEffectCategory.BENEFICIAL, 0x850b0b)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "empower"), 1.25F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_KNOCKBACK, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "empower"), 1.15F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "empower"), 0.5F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.SAFE_FALL_DISTANCE, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, "empower"), 1.25F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final Holder<MobEffect> BLESS_EFFECT = MOB_EFFECTS.register("bless",
            () -> new EffectBless(MobEffectCategory.BENEFICIAL, 0xffbf00)
    );

    public static final Holder<MobEffect> INVIGORATE_EFFECT = MOB_EFFECTS.register("invigorate",
            () -> new EffectInvigorate(MobEffectCategory.BENEFICIAL, 0xffbf00)
    );
}
