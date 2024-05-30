package com.mr_toad.h_plus.common.entity.monster;

import com.mr_toad.h_plus.common.entity.monster.variant.DesertSkeletonVariant;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.core.config.HPConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DesertSkeleton extends AbstractSkeleton implements VariantHolder<DesertSkeletonVariant> {

    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(DesertSkeleton.class, EntityDataSerializers.INT);
    
    public DesertSkeleton(EntityType<? extends AbstractSkeleton> etas, Level lvl) {
        super(etas, lvl);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TYPE, 0);
        super.defineSynchedData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putString("Variant", this.getVariant().getSerializedName());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.setVariant(DesertSkeletonVariant.byName(nbt.getString("Variant")));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    public DesertSkeletonVariant getVariant() {
        return DesertSkeletonVariant.byId(this.entityData.get(TYPE));
    }

    @Override
    public void setVariant(DesertSkeletonVariant variant) {
        this.entityData.set(TYPE, variant.getId());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag nbt) {
        DesertSkeletonVariant variant = this.level.getBiome(this.blockPosition()).is(BiomeTags.IS_BADLANDS) ? DesertSkeletonVariant.BADLANDS : DesertSkeletonVariant.DESERT;
        this.setVariant(variant);
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, groupData, nbt);
    }
    
    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    public static AttributeSupplier.Builder createDSAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    public static boolean checkDSSpawnRules(EntityType<? extends Monster> et, ServerLevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return HPMiscUtils.baseCheckEntitySpawnRules(et, accessor, type, pos, rand, HPConfig.canBonySandstoneSpawn.get());
    }
}
