package com.mr_toad.h_plus.core.mixin.global.entity;

import com.mr_toad.lib.api.util.DifficultyPredicates;
import com.mr_toad.lib.api.util.time.IntegerCooldown;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.profiling.ProfilerFiller;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(Skeleton.class)
@ParametersAreNonnullByDefault
public abstract class SkeletonMixin extends AbstractSkeleton implements RangedAttackMob {

    @Unique public final IntegerCooldown h_$specialShootCooldown = new IntegerCooldown(500, "SpecialShootCooldown");
    @Unique public boolean h_$shooted = false;

    protected SkeletonMixin(EntityType<? extends AbstractSkeleton> etas, Level lvl) {
        super(etas, lvl);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void specialShootTick(CallbackInfo ci) {
        if (DifficultyPredicates.isHard(this.level) && this.isEffectiveAi()) {
            ProfilerFiller profilerFiller = this.level.getProfiler();
            profilerFiller.push("skeleton_special_shoot");
            if (this.h_$specialShootCooldown.getCooldown() > 0) {
                this.h_$specialShootCooldown.tickDown();
            }
            profilerFiller.pop();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (DifficultyPredicates.isHard(this.level) && this.h_$shooted && this.isEffectiveAi()) {
            this.h_$specialShootCooldown.reset();
        }
    }

    
    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, v);

        if (this.getMainHandItem().getItem() instanceof BowItem) {
            abstractarrow = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrow);
        }

        if (DifficultyPredicates.isHard(this.level) && this.h_$canPerform()) {
            abstractarrow = this.h_$getSpecialArrow(itemstack, v);
        }

        double d0 = livingEntity.getX() - this.getX();
        double d1 = livingEntity.getY(0.3333333333333333) - abstractarrow.getY();
        double d2 = livingEntity.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224, d2, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrow);

        if (DifficultyPredicates.isHard(this.level) && this.h_$canPerform()) {
            this.h_$shooted = true;
        }
    }

    @Unique protected AbstractArrow h_$getSpecialArrow(ItemStack s, float v) {
        AbstractArrow abstractArrow = super.getArrow(s, v + 2.0F);
        if (abstractArrow instanceof Arrow arrow) {
            if (this.getRandom().nextBoolean()) {
                arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 200));
            } else {
                arrow.addEffect(new MobEffectInstance(MobEffects.HARM, 1));
            }
        }

        return abstractArrow;
    }

    @Unique private boolean h_$canPerform() {
        return this.h_$specialShootCooldown.getCooldown() <= 0;
    }
}
