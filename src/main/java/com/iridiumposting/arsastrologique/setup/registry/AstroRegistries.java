package com.iridiumposting.arsastrologique.setup.registry;

import net.neoforged.bus.api.IEventBus;

public class AstroRegistries {

    public static void registers(IEventBus modEventBus) {
        AstroItems.ITEMS.register(modEventBus);
        AstroCreativeTabs.TABS.register(modEventBus);
        AstroEffects.EFFECTS.register(modEventBus);
        AstroEntities.ENTITIES.register(modEventBus);
        AstroDataComponents.DATA.register(modEventBus);
    }

}
