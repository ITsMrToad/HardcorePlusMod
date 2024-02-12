package com.mr_toad.h_plus.core.mixin.global.entity;

import com.mr_toad.h_plus.common.util.entitydata.ZombieConversionDataContainer;
import com.mr_toad.h_plus.core.init.HPEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster implements ZombieConversionDataContainer {

    @Unique private int h_$inPowderSnowTime;
    @Unique private int h_$conversionTime;

    protected ZombieMixin(EntityType<? extends Monster> etm, Level lvl) {
        super(etm, lvl);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void conversionTick(CallbackInfo ci) {
        if (!this.level.isClientSide && this.isAlive() && !this.isNoAi()) {
            if (this.isInPowderSnow) {
                if (this.isFreezeConversion()) {
                    --this.h_$conversionTime;
                    if (this.h_$conversionTime < 0) {
                        this.h_$convert();
                    }
                } else {
                    ++this.h_$inPowderSnowTime;
                    if (this.h_$inPowderSnowTime >= 140) {
                        this.h_$startFreezeConversion(300);
                    }
                }
            } else {
                this.h_$inPowderSnowTime = -1;
                this.setFreezeConversion(false);
            }
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineConversionSynchedData(CallbackInfo ci) {
        this.getData().define(MARTYR_CONVERSION, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalConversionSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("MartyrConversionTime", this.isFreezeConversion() ? this.h_$conversionTime : -1);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalConversionSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("MartyrConversionTime", 99) && nbt.getInt("MartyrConversionTime") > -1) {
            this.h_$startFreezeConversion(nbt.getInt("MartyrConversionTime"));
        }
    }


    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public @NotNull SynchedEntityData getData() {
        return this.entityData;
    }


    @Unique private void h_$startFreezeConversion(int time) {
        this.h_$conversionTime = time;
        this.setFreezeConversion(true);
    }

    @Unique protected void h_$convert() {
        this.convertTo(HPEntityType.FROSTED_ZOMBIE.get(), true);
        if (!this.isSilent()) {
            this.level.levelEvent((Player)null, 1048, this.blockPosition(), 0);
        }

    }

}
