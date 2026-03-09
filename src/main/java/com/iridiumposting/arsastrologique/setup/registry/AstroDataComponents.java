package com.iridiumposting.arsastrologique.setup.registry;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.trading.MerchantOffers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AstroDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ArsAstrologique.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MerchantOffers>> ENCHANTER_TRADES =
            DATA.register("trades", () -> DataComponentType.<MerchantOffers>builder().persistent(MerchantOffers.CODEC).networkSynchronized(MerchantOffers.STREAM_CODEC).build());
}
