package com.mr_toad.h_plus.common.entity.illager.ai.goal;

import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class PillagerRetreatGoal extends RandomStrollGoal {

    private final Pillager pillager;

    public PillagerRetreatGoal(Pillager mob) {
        super(mob, PillagerRetreatGoal.calcSpeed(mob));
        this.pillager = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.isUsingItem() && this.mob.getUseItem().getItem() instanceof CrossbowItem && this.mob.getTarget() != null && !CrossbowItem.isCharged(this.mob.getUseItem()) && this.findPosition();
    }

    @Override
    public boolean canContinueToUse() {
        return !CrossbowItem.isCharged(this.mob.getUseItem()) && this.mob.isUsingItem() && this.mob.getUseItem().getItem() instanceof CrossbowItem && !this.mob.isVehicle();
    }

    @Override
    public void start() {
        super.start();
        this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, item -> item instanceof CrossbowItem));
        if (this.mob.getTarget() != null) {
            this.mob.lookAt(this.mob.getTarget(), 30.0F, 30.0F);
            this.mob.getLookControl().setLookAt(this.mob.getTarget(), 30.0F, 30.0F);
        }
        this.pillager.setChargingCrossbow(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.releaseUsingItem();
        this.pillager.setChargingCrossbow(false);
    }

    @Override
    public void tick() {
        int i = this.mob.getTicksUsingItem();
        ItemStack itemstack = this.mob.getUseItem();
        if (i >= CrossbowItem.getChargeDuration(itemstack)) {
            this.mob.releaseUsingItem();
            this.pillager.setChargingCrossbow(false);
        }
    }

    @Override
    @Nullable
    protected Vec3 getPosition() {
        return DefaultRandomPos.getPosAway(this.mob, 16, 7, this.mob.getTarget().position());
    }

    public static double calcSpeed(Pillager pillager) {
        double speed = 0.7D;
        return pillager.hasActiveRaid() ? speed + 0.1D : speed;
    }

    public boolean findPosition() {
        Vec3 vector3d = this.getPosition();
        if (vector3d == null) {
            return false;
        } else {
            this.wantedX = vector3d.x;
            this.wantedY = vector3d.y;
            this.wantedZ = vector3d.z;
            return true;
        }
    }

}
