package com.iridiumposting.arsastrologique.datagen;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.setup.registry.AstroEntities;
import com.iridiumposting.arsastrologique.common.worldgen.AstroConfiguredFeatures;
import com.iridiumposting.arsastrologique.common.worldgen.AstroPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AstroWorldgenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, AstroConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, AstroPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifierRegistry::bootstrap);

    public AstroWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ArsAstrologique.MODID));
    }

    public static class BiomeModifierRegistry {
        public static final ResourceKey<BiomeModifier> ENCHANTER_SPAWN = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ArsAstrologique.prefix("enchanter_spawn"));

        public static void bootstrap(BootstrapContext<BiomeModifier> context) {
            HolderSet<Biome> OVERWORLD_TAG = context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD);
            context.register(ENCHANTER_SPAWN, BiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(OVERWORLD_TAG, new MobSpawnSettings.SpawnerData(AstroEntities.ENCHANTER_ENTITY.get(),
                    5, 1, 2)));
        }
    }
}
