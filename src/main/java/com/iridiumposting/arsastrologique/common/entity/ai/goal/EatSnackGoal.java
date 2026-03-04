package com.iridiumposting.arsastrologique.common.entity.ai.goal;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EatSnackGoal extends Goal {
    private final EnchanterEntity enchanter;
    private int tick;

    public EatSnackGoal(EnchanterEntity enchanter) {
        this.enchanter = enchanter;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.enchanter.getMainHandItem().is(ItemsRegistry.SOURCE_BERRY_ROLL.asItem())
                || this.enchanter.getMainHandItem().is(BlockRegistry.SOURCEBERRY_BUSH.asItem())
                || this.enchanter.getMainHandItem().is(BlockRegistry.BASTION_POD.asItem());
    }

    @Override
    public boolean canContinueToUse() {
        return this.tick > 0;
    }

    @Override
    public void start() {
        this.enchanter.getNavigation().stop();
        this.tick = 20;
        this.enchanter.getEntityData().set(EnchanterEntity.EATING, true);
    }

    @Override
    public void tick() {
        super.tick();
        this.enchanter.eatSnack();
        this.tick--;
    }

    @Override
    public void stop() {
        this.enchanter.getMainHandItem().shrink(1);
        this.enchanter.reloadOffers();
        this.enchanter.getEntityData().set(EnchanterEntity.EATING, false);
    }
}
