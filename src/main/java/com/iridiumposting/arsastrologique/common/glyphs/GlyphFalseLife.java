package com.iridiumposting.arsastrologique.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.iridiumposting.arsastrologique.ArsAstrologique.prefix;

public class GlyphFalseLife extends AbstractEffect implements IPotionEffect {
    public static GlyphFalseLife INSTANCE = new GlyphFalseLife(prefix("glyph_false_life"), "False Life");

    public GlyphFalseLife(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if(rayTraceResult.getEntity() instanceof LivingEntity living){
            this.applyConfigPotion(living, MobEffects.ABSORPTION, spellStats, true);
        }
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 30);
        addExtendTimeConfig(builder, 6);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    public int getDefaultManaCost() {
        return 250;
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments(){
        return augmentSetOf(AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentAmplify.INSTANCE);
    }

//    @Override
//    protected void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
//        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 2);
//    }

    @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.NECROMANCY);
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 30 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 6 : EXTEND_TIME.get();
    }

}
