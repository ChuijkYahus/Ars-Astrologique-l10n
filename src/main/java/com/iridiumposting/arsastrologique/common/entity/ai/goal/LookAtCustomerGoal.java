package com.iridiumposting.arsastrologique.common.entity.ai.goal;

import com.iridiumposting.arsastrologique.common.entity.EnchanterEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

public class LookAtCustomerGoal extends LookAtPlayerGoal {
    private final EnchanterEntity enchanter;

    public LookAtCustomerGoal(EnchanterEntity enchanter){
        super(enchanter, Player.class, 8.0F);
        this.enchanter = enchanter;
    }

    @Override
    public boolean canUse(){
        if(this.enchanter.getTradingPlayer() != null){
            this.lookAt = this.enchanter.getTradingPlayer();
            return true;
        }
        return false;
    }
    
}
