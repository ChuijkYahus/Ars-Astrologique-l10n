package com.iridiumposting.arsastrologique.common.entity.trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnchanterUpgradeTrade implements ItemListing {
    private final ItemStack baseItem;
    private final ItemStack reagent;
    private final int reagentCost;
    private final ItemStack upgrade;
    private final int maxUses;

    public EnchanterUpgradeTrade(ItemLike baseItem, ItemLike reagent, int reagentCost, Item upgrade, int maxUses){
        this.baseItem = new ItemStack(baseItem);
        this.reagent = new ItemStack(reagent);
        this.reagentCost = reagentCost;
        this.upgrade = new ItemStack(upgrade);
        this.maxUses = maxUses;
    }

    @Nullable
    public MerchantOffer getOffer(Entity entity, RandomSource rand) {
        ItemCost baseItem = new ItemCost(this.baseItem.getItem(), 1);
        ItemCost reagent = new ItemCost(this.reagent.getItem(), this.reagentCost);
        ItemStack upgrade = new ItemStack(this.upgrade.getItem(), 1);

        return new MerchantOffer(baseItem, Optional.of(reagent), upgrade, this.maxUses, 0, 1.0F);
    }
}
