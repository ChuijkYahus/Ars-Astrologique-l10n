package com.iridiumposting.arsastrologique.common.entity;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;

public abstract class AbstractCasterEntity extends PathfinderMob implements RangedAttackMob {

    public AbstractCasterEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        /* */
    }
}
