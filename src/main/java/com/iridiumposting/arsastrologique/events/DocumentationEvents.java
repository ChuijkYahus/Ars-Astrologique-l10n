package com.iridiumposting.arsastrologique.events;

import com.hollingsworth.arsnouveau.api.documentation.ReloadDocumentationEvent;
import com.iridiumposting.arsastrologique.setup.registry.AstroDocumentation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class DocumentationEvents {
    @SubscribeEvent
    public static void addPages(ReloadDocumentationEvent.AddEntries event) {
        AstroDocumentation.init(event);
    }
}
