package com.iridiumposting.arsastrologique.setup.registry;

import com.hollingsworth.arsnouveau.setup.registry.ModEntities;
import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AstroEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ArsAstrologique.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<EnchanterEntity>> ENCHANTER_ENTITY;

    static {
        ENCHANTER_ENTITY = registerEntity("enchanter", 0.75F, 0.4F, EnchanterEntity::new, MobCategory.CREATURE);
    }

    static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(String name, float width, float height, EntityType.EntityFactory<T> factory, MobCategory kind) {
        return ENTITIES.register(name, () -> {
            EntityType.Builder<T> builder = EntityType.Builder.of(factory, kind)
                    .setTrackingRange(32)
                    .sized(width, height);
            return builder.build(ArsAstrologique.MODID + ":" + name);
        });
    }


    @EventBusSubscriber(modid = ArsAstrologique.MODID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
            event.register(ENCHANTER_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    ModEntities::genericGroundSpawn, RegisterSpawnPlacementsEvent.Operation.AND);
        }
    }
}
