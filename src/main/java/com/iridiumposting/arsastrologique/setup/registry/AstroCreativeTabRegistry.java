package com.iridiumposting.arsastrologique.setup.registry;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AstroCreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArsAstrologique.MODID);

    public static final Supplier<CreativeModeTab> ARS_ASTROLOGIQUE_TAB = TABS.register("ars_astrologique_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(AstroItemRegistry.ENCHANTER_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.ars_astrologique.ars_astrologique"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(AstroItemRegistry.ENCHANTER_SPAWN_EGG);
                    }).build());
}
