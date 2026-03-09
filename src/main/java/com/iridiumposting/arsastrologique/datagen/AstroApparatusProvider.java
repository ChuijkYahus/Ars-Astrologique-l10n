package com.iridiumposting.arsastrologique.datagen;

import com.hollingsworth.arsnouveau.common.crafting.recipes.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeBuilder;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.iridiumposting.arsastrologique.setup.registry.AstroItems;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;

import java.nio.file.Path;

public class AstroApparatusProvider extends ApparatusRecipeProvider{
    public AstroApparatusProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void collectJsons(CachedOutput pOutput) {
        recipes.add(builder()
                .withResult(AstroItems.ENCHANTER_CHARM.get())
                .withReagent(AstroItems.ENCHANTER_SHARD)
                .withPedestalItem(ItemsRegistry.ARCANIST_HOOD.get())
                .withPedestalItem(ItemsRegistry.MANIPULATION_ESSENCE)
                .withPedestalItem(ItemsRegistry.DULL_TRINKET)
                .withPedestalItem(Items.BOOK)
                .keepNbtOfReagent(true)
                .build()
        );

        Path output = this.generator.getPackOutput().getOutputFolder();
        for (ApparatusRecipeBuilder.RecipeWrapper<? extends EnchantingApparatusRecipe> g : recipes) {
            if (g != null) {
                Path path = getRecipePath(output, g.id().getPath());
                saveStable(pOutput, g.serialize(), path);
            }
        }

    }

    protected static Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/ars_astrologique/recipe/" + str + ".json");
    }
}
