package com.iridiumposting.arsastrologique.common.entity.trades;

import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

public class EssenceDoublingTrade implements ItemListing {

    private final Item essence;

    public EssenceDoublingTrade(Item essence) {
        this.essence = essence;
    }

    public MerchantOffer getOffer(Entity entity, RandomSource rand) {
        ItemCost cost = new ItemCost(ItemsRegistry.SOURCE_GEM, 1);
        ItemStack result = new ItemStack(this.essence, rand.nextIntBetweenInclusive(2, 3));
        return new MerchantOffer(cost, result, 16, 0, 1.0F);
    }
}
