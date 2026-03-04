package com.iridiumposting.arsastrologique.setup.registry;

import com.iridiumposting.arsastrologique.ArsAstrologique;
import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = ArsAstrologique.MODID)
public class AstroEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ArsAstrologique.MODID);

    public static final Supplier<EntityType<EnchanterEntity>> ENCHANTER_ENTITY =
            ENTITY_TYPES.register("enchanter", () -> EntityType.Builder.of(EnchanterEntity::new, MobCategory.CREATURE)
                    .sized(0.75F, 0.9F)
                    .setTrackingRange(16)
                    .build("enchanter"));

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ENCHANTER_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, com.hollingsworth.arsnouveau.setup.registry.ModEntities::genericGroundSpawn, RegisterSpawnPlacementsEvent.Operation.AND);
    }

}
