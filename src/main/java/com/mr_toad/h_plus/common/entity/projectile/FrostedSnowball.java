package com.mr_toad.h_plus.common.entity.projectile;

import com.mr_toad.h_plus.core.init.HPEntityType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class FrostedSnowball extends Snowball {

    public FrostedSnowball(EntityType<? extends Snowball> ets, Level lvl) {
        super(HPEntityType.FROSTED_SNOWBALL.get(), lvl);
    }

    public FrostedSnowball(Level lvl, LivingEntity owner) {
        super(lvl, owner);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        if (entity instanceof LivingEntity living) living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400));
        int i = entity instanceof Blaze ? 5 : 2;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)i);
    }
}
