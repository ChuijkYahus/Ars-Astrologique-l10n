package com.iridiumposting.arsastrologique.compat;

import alexthw.ars_elemental.registry.ModItems;
import com.iridiumposting.arsastrologique.common.entity.trades.EnchanterUpgradeTrade;
import com.iridiumposting.arsastrologique.common.entity.trades.EssenceDoublingTrade;
import com.iridiumposting.arsastrologique.common.entity.trades.ItemListing;
import com.iridiumposting.arsastrologique.common.entity.trades.TradeOffers;

import java.util.Map;

public class ElementalCompats {

    public static void addElementalTrades(Map<TradeOffers.TRADE_LISTINGS, ItemListing[]> tradeMap) {
        TradeOffers.appendTrades(tradeMap, TradeOffers.TRADE_LISTINGS.ESSENCES,
                new EssenceDoublingTrade(ModItems.ANIMA_ESSENCE.get())
        );

        TradeOffers.appendTrades(tradeMap, TradeOffers.TRADE_LISTINGS.GEAR,
                new EnchanterUpgradeTrade(ModItems.LESSER_AIR_FOCUS.get(), ModItems.MARK_OF_MASTERY, 1, ModItems.AIR_FOCUS.get(), 1),
                new EnchanterUpgradeTrade(ModItems.LESSER_WATER_FOCUS.get(), ModItems.MARK_OF_MASTERY, 1, ModItems.WATER_FOCUS.get(), 1),
                new EnchanterUpgradeTrade(ModItems.LESSER_EARTH_FOCUS.get(), ModItems.MARK_OF_MASTERY, 1, ModItems.EARTH_FOCUS.get(), 1),
                new EnchanterUpgradeTrade(ModItems.LESSER_FIRE_FOCUS.get(), ModItems.MARK_OF_MASTERY, 1, ModItems.FIRE_FOCUS.get(), 1)
        );
    }
}
