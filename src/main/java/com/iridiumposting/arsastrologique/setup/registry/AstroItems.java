package com.iridiumposting.arsastrologique.setup.registry;

import com.hollingsworth.arsnouveau.common.items.ModItem;
import com.iridiumposting.arsastrologique.common.items.EnchanterCharm;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.iridiumposting.arsastrologique.ArsAstrologique.MODID;

public class AstroItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> ENCHANTER_SPAWN_EGG = ITEMS.register("enchanter_spawn_egg",
            () -> new DeferredSpawnEggItem(AstroEntities.ENCHANTER_ENTITY, 0x5e1d74,0xfce44d,
                    new Item.Properties()));

    public static final DeferredItem<Item> ENCHANTER_CHARM = ITEMS.register("enchanter_charm", EnchanterCharm::new);
    public static final DeferredItem<Item> ENCHANTER_SHARD = ITEMS.register("enchanter_scroll",
            () -> new ModItem());
}
