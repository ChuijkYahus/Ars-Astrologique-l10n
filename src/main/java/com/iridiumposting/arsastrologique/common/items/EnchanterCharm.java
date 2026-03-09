package com.iridiumposting.arsastrologique.common.items;

import com.hollingsworth.arsnouveau.api.item.AbstractSummonCharm;
import com.hollingsworth.arsnouveau.common.block.tile.SummoningTile;
import com.hollingsworth.arsnouveau.common.items.data.PersistentFamiliarData;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import com.iridiumposting.arsastrologique.common.entity.trades.TradeOffers;
import com.iridiumposting.arsastrologique.setup.registry.AstroDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;

public class EnchanterCharm extends AbstractSummonCharm {

    @Override
    public InteractionResult useOnBlock(UseOnContext context, Level world, BlockPos pos) {
        EnchanterEntity enchanter = new EnchanterEntity(world, true);
        enchanter.fromCharmData(context.getItemInHand().getOrDefault(DataComponentRegistry.PERSISTENT_FAMILIAR_DATA, new PersistentFamiliarData()));

        MerchantOffers charmOffers = context.getItemInHand().get(AstroDataComponents.ENCHANTER_TRADES.get());

        enchanter.offers = new MerchantOffers();
        if(charmOffers == null || charmOffers.isEmpty()) TradeOffers.updateTrades(enchanter);
        else enchanter.offers.addAll(charmOffers);

        enchanter.setPos(pos.getX(), pos.above().getY(), pos.getZ());
        world.addFreshEntity(enchanter);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOnSummonTile(UseOnContext context, Level world, SummoningTile tile, BlockPos pos) {
        return useOnBlock(context, world, pos);
    }
}
