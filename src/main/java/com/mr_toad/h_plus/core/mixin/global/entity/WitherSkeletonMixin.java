package com.mr_toad.h_plus.core.mixin.global.entity;

import com.mr_toad.h_plus.common.util.entitydata.WitherSkeletonHoldBowDataContainer;
import com.mr_toad.h_plus.core.config.HPConfig;
import com.mr_toad.lib.api.util.DifficultyPredicates;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(WitherSkeleton.class)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class WitherSkeletonMixin extends AbstractSkeleton implements RangedAttackMob, WitherSkeletonHoldBowDataContainer {

    protected WitherSkeletonMixin(EntityType<? extends AbstractSkeleton> s, Level lvl) {
        super(s, lvl);
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"))
    protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance instance, CallbackInfo ci) {}

    @Inject(method = "finalizeSpawn", at = @At("HEAD"), cancellable = true)
    private void finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType type, SpawnGroupData data, CompoundTag nbt, CallbackInfoReturnable<SpawnGroupData> cir) {
        boolean flag = HPConfig.canWitherSkeletonsSpawnWithBows.get() && this.random.nextInt(10) == 0 && DifficultyPredicates.isHard(this.getLevel());
        this.setCanHoldBow(flag);
        this.h_$addBowToSlot();
        cir.setReturnValue(cir.getReturnValue());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putBoolean("CanHoldBow", this.canHoldBow());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.setCanHoldBow(nbt.getBoolean("CanHoldBow"));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    protected void defineSynchedData() {
        this.defineWitherSkeletonHoldBowData();
        super.defineSynchedData();
    }

    @Override
    protected AbstractArrow getArrow(ItemStack stack, float f0) {
        AbstractArrow abstractarrow = super.getArrow(stack, f0);
        if (abstractarrow instanceof Arrow) {
            ((Arrow)abstractarrow).addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
        }

        return abstractarrow;
    }

    @Override
    public @NotNull SynchedEntityData getData() {
        return this.entityData;
    }

    @Unique protected void h_$addBowToSlot() {
        ItemStack stack = this.canHoldBow() ? new ItemStack(Items.BOW) : new ItemStack(Items.STONE_SWORD);
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }

}
