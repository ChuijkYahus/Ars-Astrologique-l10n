package com.iridiumposting.arsastrologique.events;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.client.renderer.EnchanterRenderer;
import com.iridiumposting.arsastrologique.setup.registry.AstroEntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = ArsAstrologique.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void bindRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AstroEntityRegistry.ENCHANTER_ENTITY.get(), EnchanterRenderer::new);
    }

}
