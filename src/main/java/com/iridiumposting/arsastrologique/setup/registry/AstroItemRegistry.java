package com.iridiumposting.arsastrologique.setup.registry;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.iridiumposting.arsastrologique.ArsAstrologique.MODID;

public class AstroItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> ENCHANTER_SPAWN_EGG = ITEMS.register("enchanter_spawn_egg",
            () -> new DeferredSpawnEggItem(AstroEntityRegistry.ENCHANTER_ENTITY, 0x5e1d74,0xfce44d,
                    new Item.Properties()));
}
