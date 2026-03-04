package com.iridiumposting.arsastrologique.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.iridiumposting.arsastrologique.setup.registry.AstroEffectRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

import static com.iridiumposting.arsastrologique.ArsAstrologique.prefix;

public class GlyphBless extends AbstractEffect implements IPotionEffect {
    public static GlyphBless INSTANCE = new GlyphBless(prefix("glyph_bless"), "Bless");

    public GlyphBless(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if(rayTraceResult.getEntity() instanceof LivingEntity living) this.applyConfigPotion(living, AstroEffectRegistry.BLESS_EFFECT, spellStats, true);
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 30);
        addExtendTimeConfig(builder, 6);
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public int getDefaultManaCost() {
        return 100;
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments(){
        return augmentSetOf(AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentAmplify.INSTANCE);
    }

    @Override
    protected void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 4);
    }

    @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ABJURATION);
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 10 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 3 : EXTEND_TIME.get();
    }

}

