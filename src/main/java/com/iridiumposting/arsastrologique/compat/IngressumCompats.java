package com.iridiumposting.arsastrologique.compat;

import com.iridiumposting.arsastrologique.common.entity.trades.ItemListing;
import com.iridiumposting.arsastrologique.common.entity.trades.TradeOffers;
import com.iridiumposting.arsingressum.common.item.data.TuningForkData;
import com.iridiumposting.arsingressum.setup.registry.AddonItemRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class IngressumCompats {

    public static void addIngressumTrades(Map<TradeOffers.TRADE_LISTINGS, ItemListing[]> tradeMap) {
        tradeMap.put(TradeOffers.TRADE_LISTINGS.TUNING_FORKS, new ItemListing[]{
                new EnchanterAttuningTrade(Level.OVERWORLD, 3),
                new EnchanterAttuningTrade(Level.NETHER, 3),
                new EnchanterAttuningTrade(Level.END, 3),
        });
    }
}

class EnchanterAttuningTrade implements ItemListing {
    private final ResourceKey<Level> targetDim;
    private final int maxUses;

    public EnchanterAttuningTrade(ResourceKey<Level> targetDim, int maxUses){
        this.targetDim = targetDim;
        this.maxUses = maxUses;
    }

    @Nullable
    public MerchantOffer getOffer(Entity entity, RandomSource rand) {
        ItemCost baseFork = new ItemCost(AddonItemRegistry.VACANT_TUNING_FORK, 1);
        ItemStack attunedFork = new ItemStack(AddonItemRegistry.ATTUNED_TUNING_FORK.asItem(), 1);

        attunedFork.set(TuningForkData.DIMENSION, this.targetDim);

        return new MerchantOffer(baseFork, attunedFork, this.maxUses, 0, 1.0F);
    }
}