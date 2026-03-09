package com.iridiumposting.arsastrologique.setup.registry;

import com.hollingsworth.arsnouveau.api.documentation.DocCategory;
import com.hollingsworth.arsnouveau.api.documentation.ReloadDocumentationEvent;
import com.hollingsworth.arsnouveau.api.documentation.builder.DocEntryBuilder;
import com.hollingsworth.arsnouveau.api.documentation.entry.EntityEntry;
import com.hollingsworth.arsnouveau.api.documentation.entry.TextEntry;
import com.hollingsworth.arsnouveau.api.registry.DocumentationRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import static com.hollingsworth.arsnouveau.setup.registry.Documentation.addPage;
import static com.iridiumposting.arsastrologique.ArsAstrologique.prefix;

public class AstroDocumentation {
    public static void init(ReloadDocumentationEvent.AddEntries ignored) {
        var enchanterCharm = addPage(
                new AstroDocEntryBuilder(DocumentationRegistry.CRAFTING, AstroItems.ENCHANTER_CHARM.get())
                        .withIntroPage()
                        .withCraftingPages(AstroItems.ENCHANTER_CHARM)
                        .withPage(EntityEntry.create(AstroEntities.ENCHANTER_ENTITY.get(), Component.translatable("ars_astrologique.page2.enchanter_charm")))
                        .withHeaderPage("ars_astrologique.page3.enchanter_charm", "ars_nouveau.summoning")
                        .withHeaderPage("ars_astrologique.page4.enchanter_charm", "ars_astrologique.trading")
                        .withHeaderPage("ars_astrologique.page5.enchanter_charm", "ars_astrologique.snacks")
        );
    }

    static class AstroDocEntryBuilder extends DocEntryBuilder {
        public AstroDocEntryBuilder(DocCategory category, String name) {
            this(category, name, prefix(name));
        }

        public AstroDocEntryBuilder(DocCategory category, String name, ResourceLocation entryId) {
            super(category, name.contains(".") ? name : "ars_astrologique.page." + name, entryId);
        }

        public AstroDocEntryBuilder(DocCategory category, ItemLike itemLike) {
            super(category, itemLike);
        }

        public DocEntryBuilder withIntroPage(String id) {
            textCounter++;
            pages.add(TextEntry.create(Component.translatable("ars_astrologique.page" + textCounter + "." + id), Component.translatable(titleKey), displayItem));
            return this;
        }
    }

}
