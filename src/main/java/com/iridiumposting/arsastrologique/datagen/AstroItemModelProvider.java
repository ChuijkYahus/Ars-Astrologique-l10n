package com.iridiumposting.arsastrologique.datagen;

import com.google.common.base.Preconditions;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.setup.registry.AstroItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;

public class AstroItemModelProvider extends ItemModelProvider {
    public AstroItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArsAstrologique.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        spawnEggItem(AstroItems.ENCHANTER_SPAWN_EGG.get());
        basicItem(AstroItems.ENCHANTER_CHARM.get());
        basicItem(AstroItems.ENCHANTER_SHARD.get());

        /* FROM ARS NOUVEAU ITEM MODEL GENERATOR */
        for (Supplier<Glyph> i : GlyphRegistry.getGlyphItemMap().values()) {
            try {
                if (i.get().spellPart.getRegistryName().getNamespace().equals(ArsAstrologique.MODID))
                    getBuilder(i.get().spellPart.getRegistryName().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", spellTexture(i.get()));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("No texture for " + i.get());
            }
        }

    }
    /* FROM ARS NOUVEAU ITEM MODEL GENERATOR */
    private ResourceLocation registryName(final Item item) {
        return Preconditions.checkNotNull(getRegistryName(item), "Item %s has a null registry name", item);
    }

    /* FROM ARS NOUVEAU ITEM MODEL GENERATOR */
    private ResourceLocation spellTexture(final Item item) {
        final ResourceLocation name = registryName(item);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "item" + "/" + name.getPath().replace("glyph_", ""));
    }
}
