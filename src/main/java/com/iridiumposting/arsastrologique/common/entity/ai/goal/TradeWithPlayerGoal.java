package com.iridiumposting.arsastrologique.common.entity.ai.goal;

import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class TradeWithPlayerGoal extends Goal {
    private final EnchanterEntity enchanter;

    public TradeWithPlayerGoal(EnchanterEntity enchanter) {
        this.enchanter = enchanter;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.enchanter.isAlive()) {
            return false;
        } else if (this.enchanter.isInWater()) {
            return false;
        } else if (!this.enchanter.onGround()) {
            return false;
        } else if (this.enchanter.hurtMarked) {
            return false;
        } else {
            Player player = this.enchanter.getTradingPlayer();
            if (player == null) {
                return false;
            } else return !(this.enchanter.distanceToSqr(player) > (double) 16.0F);
        }
    }

    public void start() {
        this.enchanter.getNavigation().stop();
    }

    public void stop() {
        this.enchanter.setTradingPlayer(null);
    }
}
