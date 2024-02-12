package com.mr_toad.h_plus.common.entity.monster.ai.goal;

import com.mr_toad.lib.api.entity.ai.goal.StealHPGoal;
import com.mr_toad.lib.api.util.DifficultyPredicates;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;

import java.util.function.Predicate;

public class ZombieStealHPGoal extends StealHPGoal {

    private final Zombie zombie;
    private int raiseArmTicks;

    public static final Predicate<Entity> ANIMALS = entity -> entity.getType() == EntityType.COW || entity.getType() == EntityType.PIG || entity.getType() == EntityType.SHEEP || entity.getType() == EntityType.HORSE || entity.getType() == EntityType.DONKEY || entity.getType() == EntityType.MULE;

    public ZombieStealHPGoal(Zombie mob) {
        super(mob, 1.0D, false, true, hp -> hp < 10.0F, ZombieStealHPGoal.ANIMALS, DifficultyPredicates.HARD_PREDICATE, 2.0F);
        this.zombie = mob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && DifficultyPredicates.isHard(this.zombie.level);
    }

    @Override
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        this.zombie.setAggressive(this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2);
    }


}
