package com.mr_toad.h_plus.common.entity.monster.ai.goal;

import com.mr_toad.lib.api.entity.ai.goal.MobFollowAdultMobGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;

public class BabySpidersFollowParentGoal<E extends Mob> extends MobFollowAdultMobGoal {

    @SuppressWarnings("unchecked")
    public BabySpidersFollowParentGoal(Monster child, Class<E> eClass) {
        super(child, (Class<Mob>) eClass,  0.8D, dist -> !(dist < 9.0D) && !(dist > 256.0D));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.child.getTarget() == null;
    }
}
