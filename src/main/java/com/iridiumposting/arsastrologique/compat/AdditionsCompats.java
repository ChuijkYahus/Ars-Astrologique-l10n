package com.iridiumposting.arsastrologique.compat;

import com.github.jarva.arsadditions.setup.registry.AddonItemRegistry;
import com.iridiumposting.arsastrologique.common.entity.trades.*;
import net.minecraft.world.item.Items;

import java.util.Map;

public class AdditionsCompats {

    public static void addAdditionsTrades(Map<TradeOffers.TRADE_LISTINGS, ItemListing[]> tradeMap) {
        TradeOffers.appendTrades(tradeMap, TradeOffers.TRADE_LISTINGS.SPELLS,
                new EnchanterUpgradeTrade(AddonItemRegistry.CODEX_ENTRY.asItem(), Items.BLAZE_ROD, 3, AddonItemRegistry.CODEX_ENTRY_LOST.asItem(), 16),
                new EnchanterUpgradeTrade(AddonItemRegistry.CODEX_ENTRY_LOST.asItem(), Items.NETHER_STAR, 1, AddonItemRegistry.CODEX_ENTRY_ANCIENT.asItem(),16)
        );
    }
}
