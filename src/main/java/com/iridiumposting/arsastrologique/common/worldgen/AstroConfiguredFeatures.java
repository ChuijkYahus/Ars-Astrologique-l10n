package com.iridiumposting.arsastrologique.common.worldgen;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class AstroConfiguredFeatures {

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        /* TO BE ADDED */
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(ArsAstrologique.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key,
                                                                                          F feature,
                                                                                          FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
