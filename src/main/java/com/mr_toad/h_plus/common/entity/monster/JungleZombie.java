package com.mr_toad.h_plus.common.entity.monster;

import com.mr_toad.h_plus.common.entity.monster.variant.JungleZombieVariant;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.core.config.HPConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class JungleZombie extends Zombie implements VariantHolder<JungleZombieVariant> {

    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(JungleZombie.class, EntityDataSerializers.INT);

    public JungleZombie(EntityType<? extends Zombie> etz, Level lvl) {
        super(etz, lvl);
    }

    @Override
    protected void addBehaviourGoals() {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Ocelot.class, true));
        super.addBehaviourGoals();
    }

    public static boolean checkJZSpawnRules(EntityType<? extends Monster> et, ServerLevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return HPMiscUtils.baseCheckEntitySpawnRules(et, accessor, type, pos, rand, HPConfig.canPutridSpawn.get());
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
        this.setVariant(JungleZombieVariant.byName(nbt.getString("Variant")));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public JungleZombieVariant getVariant() {
        return JungleZombieVariant.byId(this.entityData.get(TYPE));
    }

    @Override
    public void setVariant(JungleZombieVariant variant) {
        this.entityData.set(TYPE, variant.getId());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficultyInstance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag nbt) {
        int i = this.getRandom().nextInt(75);
        JungleZombieVariant variant = i > 35 ? JungleZombieVariant.VAR_B : JungleZombieVariant.VAR_A;
        this.setVariant(variant);
        return super.finalizeSpawn(accessor, difficultyInstance, spawnType, groupData, nbt);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!super.doHurtTarget(entity)) {
            return false;
        } else {
            if (entity instanceof LivingEntity living && living.getType() != EntityType.IRON_GOLEM) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 200), this);
            }
            return true;
        }
    }
}
