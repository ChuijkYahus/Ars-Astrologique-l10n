package com.iridiumposting.arsastrologique.common.entity.trades;

import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

public class ItemsForSourceGems implements ItemListing {

    private final ItemStack itemStack;
    private final int costCountMin;
    private final int costCountMax;
    private final int resultCountMin;
    private final int resultCountMax;
    private final int maxUses;
    private final float priceMultiplier;

    public ItemsForSourceGems(Item item, int costMin, int costMax, int resultCountMin, int resultCountMax, int maxUses) {
        this(new ItemStack(item), costMin, costMax, resultCountMin, resultCountMax, maxUses, 1.0F);
    }

    public ItemsForSourceGems(ItemStack item, int costMin, int costMax, int resultCountMin, int resultCountMax, int maxUses, float priceMultiplier) {
        this.itemStack = item;
        this.costCountMin = costMin;
        this.costCountMax = costMax;
        this.resultCountMin = resultCountMin;
        this.resultCountMax = resultCountMax;
        this.maxUses = maxUses;
        this.priceMultiplier = priceMultiplier;
    }

    public MerchantOffer getOffer(Entity entity, RandomSource rand) {
        ItemCost cost = new ItemCost(ItemsRegistry.SOURCE_GEM, rand.nextIntBetweenInclusive(this.costCountMin, this.costCountMax));
        ItemStack result = new ItemStack(this.itemStack.getItem(), rand.nextIntBetweenInclusive(this.resultCountMin, this.resultCountMax));
        return new MerchantOffer(cost, result, this.maxUses, 0, this.priceMultiplier);
    }
}
