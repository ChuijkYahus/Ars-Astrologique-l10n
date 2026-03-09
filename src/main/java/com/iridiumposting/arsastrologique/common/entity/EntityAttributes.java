package com.iridiumposting.arsastrologique.common.entity;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.setup.registry.AstroEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = ArsAstrologique.MODID)
public class EntityAttributes {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(AstroEntities.ENCHANTER_ENTITY.get(), EnchanterEntity.createAttributes());
    }
}
