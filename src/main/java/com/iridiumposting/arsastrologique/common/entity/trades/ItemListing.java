package com.iridiumposting.arsastrologique.common.entity.trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

public interface ItemListing {
    @Nullable MerchantOffer getOffer(Entity entity, RandomSource var);
}
