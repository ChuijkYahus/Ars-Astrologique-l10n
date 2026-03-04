package com.iridiumposting.arsastrologique.setup.registry;

import net.neoforged.bus.api.IEventBus;

public class AstroRegistrySetup {

    public static void registers(IEventBus modEventBus) {
        AstroItemRegistry.ITEMS.register(modEventBus);
        AstroCreativeTabRegistry.TABS.register(modEventBus);
        AstroEffectRegistry.MOB_EFFECTS.register(modEventBus);
        AstroEntityRegistry.ENTITY_TYPES.register(modEventBus);
    }

}
