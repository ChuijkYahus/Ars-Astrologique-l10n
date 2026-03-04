package com.iridiumposting.arsastrologique.datagen;

import com.alexthw.sauce.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.datagen.GlyphRecipeProvider;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.iridiumposting.arsastrologique.common.glyphs.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.nio.file.Path;

import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;

public class AstroGlyphRecipeProvider extends GlyphRecipeProvider {

    public AstroGlyphRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void collectJsons(CachedOutput cache) {
        addRecipe(GlyphAlacrity.INSTANCE, ItemsRegistry.ABJURATION_ESSENCE, Blocks.BEACON.asItem(), Items.GOLDEN_PICKAXE, Items.SUGAR, Items.SUGAR);
        addRecipe(GlyphBless.INSTANCE, ItemsRegistry.ABJURATION_ESSENCE, Items.GHAST_TEAR, Items.GOLDEN_APPLE, Items.GLISTERING_MELON_SLICE);
        addRecipe(GlyphEmpower.INSTANCE, ItemsRegistry.ABJURATION_ESSENCE, Items.DIAMOND_SWORD, Blocks.IRON_BLOCK.asItem(), Items.BLAZE_ROD, Items.BLAZE_ROD);
        addRecipe(GlyphFalseLife.INSTANCE, ModRegistry.ANIMA_ESSENCE.get(), Items.GOLDEN_APPLE, Items.GOLDEN_APPLE, Items.GOLDEN_APPLE);
        addRecipe(GlyphInvigorate.INSTANCE, ModRegistry.ANIMA_ESSENCE.get(), ItemsRegistry.ABJURATION_ESSENCE, Items.GOLDEN_CARROT, Items.GOLDEN_CARROT, Items.GOLDEN_CARROT);
        addRecipe(GlyphStalwart.INSTANCE, ItemsRegistry.ABJURATION_ESSENCE, Blocks.NETHERITE_BLOCK.asItem(), BlockRegistry.BASTION_POD.asItem());

        for (GlyphRecipe recipe : recipes) {
            Path path = getScribeGlyphPath(output, recipe.output.getItem());
            saveStable(cache, GlyphRecipe.CODEC.encodeStart(JsonOps.INSTANCE, recipe).getOrThrow(), path);
        }
    }

    public GlyphRecipe addRecipe(AbstractSpellPart part, ItemLike... items) {
        var builder = get(part);
        for( ItemLike item : items)
            builder.withItem(item);
        recipes.add(builder);
        return builder;
    }

    protected static Path getScribeGlyphPath(Path pathIn, Item glyph) {
        return pathIn.resolve("data/ars_astrologique/recipe/" + getRegistryName(glyph).getPath() + ".json");
    }
}
