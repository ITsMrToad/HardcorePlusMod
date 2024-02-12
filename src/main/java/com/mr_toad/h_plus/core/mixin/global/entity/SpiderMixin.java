package com.mr_toad.h_plus.core.mixin.global.entity;

import com.mr_toad.h_plus.common.util.entitydata.SpiderSpawnsDataContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Spider.class)
public abstract class SpiderMixin extends Monster implements SpiderSpawnsDataContainer {

    protected SpiderMixin(EntityType<? extends Monster> etm, Level lvl) {
        super(etm, lvl);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void define(CallbackInfo ci) {
        this.defineSpidersSpawnData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putBoolean("CanSpawn", this.canSpawn());
        nbt.putInt("BabySpidersSpawnCount", this.getSpidersCount());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.setSpidersCount(nbt.getInt("BabySpidersSpawnCount"));
        this.setCanSpawn(nbt.getBoolean("CanSpawn"));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public @NotNull SynchedEntityData getData() {
        return this.getEntityData();
    }
}
