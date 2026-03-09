package com.iridiumposting.arsastrologique.datagen;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ArsAstrologique.MODID)
public class AstroDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new AstroItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new AstroApparatusProvider(generator));
        generator.addProvider(event.includeServer(), new AstroGlyphRecipeProvider(generator));
        generator.addProvider(event.includeServer(), new AstroWorldgenProvider(packOutput, lookupProvider));
    }
}
