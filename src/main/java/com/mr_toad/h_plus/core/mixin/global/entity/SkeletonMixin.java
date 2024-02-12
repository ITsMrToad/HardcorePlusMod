package com.mr_toad.h_plus.core.mixin.global.entity;

import com.mr_toad.h_plus.core.config.HPConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(Skeleton.class)
@ParametersAreNonnullByDefault
public abstract class SkeletonMixin extends AbstractSkeleton implements RangedAttackMob {

    @Unique private int h_$cooldown = 500;
    @Unique public int h_$ammoShooted = 0;
    @Unique public boolean h_$hasBeenPerformed;

    protected SkeletonMixin(EntityType<? extends AbstractSkeleton> etas, Level lvl) {
        super(etas, lvl);
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        if (this.h_$ammoShooted < 6) {
            ++this.h_$ammoShooted;
        } else {
            this.h_$ammoShooted = 0;
        }

        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowItem)));
        AbstractArrow abstractarrow = this.h_$canPerform() ? this.h_$getSpecialArrow(itemstack, v) : this.getArrow(itemstack, v);
        if (this.getMainHandItem().getItem() instanceof BowItem) abstractarrow = ((BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrow);

        double d0 = livingEntity.getX() - this.getX();
        double d1 = livingEntity.getY(0.3333333333333333) - abstractarrow.getY();
        double d2 = livingEntity.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrow);


    }

    @Override
    public void tick() {
        if (this.h_$cooldown > 0) {
            --this.h_$cooldown;
            this.h_$hasBeenPerformed = false;
        } else {
            this.h_$hasBeenPerformed = true;
        }
        super.tick();
    }

    @Unique protected AbstractArrow h_$getSpecialArrow(ItemStack s, float v) {
        AbstractArrow abstractArrow = super.getArrow(s, v + 2.0F);
        if (abstractArrow instanceof Arrow arrow) {
            if (this.getRandom().nextBoolean()) {
                arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
            } else {
                arrow.addEffect(new MobEffectInstance(MobEffects.HARM, 10));
            }
        }

        return abstractArrow;
    }

    @Unique public boolean h_$canPerform() {
        if (this.h_$cooldown <= 0 && HPConfig.canSkeletonsUseSpecialArrows.get()) {
            this.h_$cooldown = 0;
            if (this.h_$hasBeenPerformed) {
                this.h_$cooldown = 1000;
            }
            return this.h_$ammoShooted > 5;
        }
        return false;
    }

}
