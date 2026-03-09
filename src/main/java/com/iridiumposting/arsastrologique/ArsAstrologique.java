package com.iridiumposting.arsastrologique;

import com.iridiumposting.arsastrologique.setup.registry.AstroRegistries;
import com.iridiumposting.arsastrologique.setup.registry.ArsNouveauRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArsAstrologique.MODID)
public class ArsAstrologique {
    public static final String MODID = "ars_astrologique";

    public static final Logger LOGGER = LogManager.getLogger();

    public ArsAstrologique(IEventBus modEventBus) {
        
        AstroRegistries.registers(modEventBus);
        ArsNouveauRegistry.init();
        
        modEventBus.addListener(this::common);
        modEventBus.addListener(this::client);
        
        NeoForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void common(final FMLCommonSetupEvent event) {
        /* NOTHING TO SEE YET. */
    }

    private void client(final FMLClientSetupEvent event) {
        /* NOTHING TO SEE YET. */
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

}
