package com.iridiumposting.arsastrologique.common.entity.trades;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import com.iridiumposting.arsastrologique.compat.AdditionsCompats;
import com.iridiumposting.arsastrologique.compat.ElementalCompats;
import com.iridiumposting.arsastrologique.compat.IngressumCompats;
import net.minecraft.Util;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.fml.ModList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TradeOffers {

    public enum TRADE_LISTINGS {
        ESSENCES,
        GEAR,
        SPELLS,
        TUNING_FORKS
    }

    public static final Map<TRADE_LISTINGS, ItemListing[]> TRADES_BY_TYPE = Util.make(Maps.newHashMap(), (map) -> {
        map.put(TRADE_LISTINGS.ESSENCES, new ItemListing[]{
                new EssenceDoublingTrade(ItemsRegistry.ABJURATION_ESSENCE.asItem()),
                new EssenceDoublingTrade(ItemsRegistry.CONJURATION_ESSENCE.asItem()),
                new EssenceDoublingTrade(ItemsRegistry.MANIPULATION_ESSENCE.asItem()),
                new EssenceDoublingTrade(ItemsRegistry.FIRE_ESSENCE.asItem()),
                new EssenceDoublingTrade(ItemsRegistry.WATER_ESSENCE.asItem()),
                new EssenceDoublingTrade(ItemsRegistry.EARTH_ESSENCE.asItem()),
                new EssenceDoublingTrade(ItemsRegistry.AIR_ESSENCE.asItem())
        });

        map.put(TRADE_LISTINGS.GEAR, new ItemListing[]{
                new ItemsForSourceGems(ItemsRegistry.SHAPERS_FOCUS.asItem(), 16, 48, 1, 1, 1),
                new ItemsForSourceGems(ItemsRegistry.SUMMONING_FOCUS.asItem(), 16, 48, 1, 1, 1),

                new ItemsForSourceGems(ItemsRegistry.WAND.asItem(), 5, 8, 1, 1, 1),
                new EnchanterUpgradeTrade(Items.DIAMOND_SWORD, BlockRegistry.SOURCE_GEM_BLOCK.asItem(), 2, ItemsRegistry.ENCHANTERS_SWORD.asItem(), 1),
                new EnchanterUpgradeTrade(Items.GOLD_INGOT, BlockRegistry.SOURCE_GEM_BLOCK.asItem(), 2, ItemsRegistry.ENCHANTERS_MIRROR.asItem(), 1),

                new EnchanterUpgradeTrade(Items.IRON_HELMET, ItemsRegistry.MAGE_FIBER, 2, ItemsRegistry.ARCANIST_HOOD.asItem(), 1),
                new EnchanterUpgradeTrade(Items.IRON_CHESTPLATE, ItemsRegistry.MAGE_FIBER, 3, ItemsRegistry.ARCANIST_ROBES.asItem(), 1),
                new EnchanterUpgradeTrade(Items.IRON_LEGGINGS, ItemsRegistry.MAGE_FIBER, 3, ItemsRegistry.ARCANIST_LEGGINGS.asItem(), 1),
                new EnchanterUpgradeTrade(Items.IRON_BOOTS, ItemsRegistry.MAGE_FIBER, 1, ItemsRegistry.ARCANIST_BOOTS.asItem(), 1),
        });
    });

    public static void updateTrades(EnchanterEntity trader) {
        Map<TRADE_LISTINGS, ItemListing[]> trades = copyBaseTrades();

        if(ModList.get().isLoaded("ars_additions"))
            AdditionsCompats.addAdditionsTrades(trades);
        if(ModList.get().isLoaded("ars_elemental"))
            ElementalCompats.addElementalTrades(trades);
        if(ModList.get().isLoaded("ars_ingressum"))
            IngressumCompats.addIngressumTrades(trades);

        chooseRandomTrades(trader, trades.get(TRADE_LISTINGS.ESSENCES), 4);
        chooseRandomTrades(trader, trades.get(TRADE_LISTINGS.GEAR), 4);
        chooseRandomTrades(trader, trades.get(TRADE_LISTINGS.SPELLS), 2);
        chooseRandomTrades(trader, trades.get(TRADE_LISTINGS.TUNING_FORKS), 2);
    }

    private static void chooseRandomTrades(EnchanterEntity trader, ItemListing[] itemListings, int maxOffers) {
        if(itemListings == null || itemListings.length == 0) return;

        HashSet<Integer> chosenIndices = Sets.newHashSet();
        if(itemListings.length > maxOffers)
            while(chosenIndices.size() < maxOffers) chosenIndices.add(trader.getRandom().nextInt(itemListings.length));
        else for(int i = 0; i < itemListings.length; ++i)
            chosenIndices.add(i);

        for(Integer index : chosenIndices) {
            ItemListing itemListing = itemListings[index];
            MerchantOffer merchantOffer = itemListing.getOffer(trader, trader.getRandom());
            if(merchantOffer == null) continue;
            trader.getOffers().add(merchantOffer);
        }
    }

    public static void appendTrades(Map<TRADE_LISTINGS, ItemListing[]> map, TRADE_LISTINGS key, ItemListing... toAdd) {
        map.merge(key, toAdd, (existing, added) -> {
            ItemListing[] combined = new ItemListing[existing.length + added.length];
            System.arraycopy(existing, 0, combined, 0, existing.length);
            System.arraycopy(added, 0, combined, existing.length, added.length);
            return combined;
        });
    }

    private static Map<TRADE_LISTINGS, ItemListing[]> copyBaseTrades() {
        Map<TRADE_LISTINGS, ItemListing[]> copy = new HashMap<>();
        TRADES_BY_TYPE.forEach((k,v) -> copy.put(k,v.clone()));
        return copy;
    }
}
