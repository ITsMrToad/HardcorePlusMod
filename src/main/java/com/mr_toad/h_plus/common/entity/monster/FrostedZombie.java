package com.mr_toad.h_plus.common.entity.monster;

import com.mr_toad.h_plus.common.entity.monster.variant.FrostedZombieVariant;
import com.mr_toad.h_plus.common.entity.projectile.FrostedSnowball;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.core.config.HPConfig;
import com.mr_toad.h_plus.core.init.HPSoundEvents;
import com.mr_toad.lib.api.entity.HybridAttackType;
import com.mr_toad.lib.api.entity.entitydata.HybridAttackDataContainer;
import com.mr_toad.lib.api.entity.entitydata.ToadlyEntityDataSerializers;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import org.jetbrains.annotations.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FrostedZombie extends Zombie implements VariantHolder<FrostedZombieVariant>, RangedAttackMob, HybridAttackDataContainer {

    private static final  EntityDataAccessor<HybridAttackType> ATTACK_TYPE = SynchedEntityData.defineId(FrostedZombie.class, ToadlyEntityDataSerializers.HYBRID_ATTACK_TYPE);
    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(FrostedZombie.class, EntityDataSerializers.INT);

    private final ZombieAttackGoal zombieAttackGoal = new ZombieAttackGoal(this, 1.0D, false);
    private final RangedAttackGoal rangedAttackGoal = new RangedAttackGoal(this, 1.0D, 7, 11, 14.0F);

    public FrostedZombie(EntityType<? extends Zombie> etz, Level lvl) {
        super(etz, lvl);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    public static AttributeSupplier.Builder createFrostedZombieAttributes() {
        return Zombie.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.18D);
    }

    public static boolean checkFZSpawnRules(EntityType<? extends Monster> et, ServerLevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return HPMiscUtils.baseCheckEntitySpawnRules(et, accessor, type, pos, rand, HPConfig.canMartyrSpawn.get());
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isEffectiveAi() && this.level instanceof ServerLevel serverLevel) {
            ResourceKey<Level> resourcekey = serverLevel.dimension();
            if (resourcekey == ServerLevel.NETHER) {
                this.setSecondsOnFire(4);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        LivingEntity target = this.getTarget();
        if (target != null && this.isEffectiveAi()) {
            if (this.distanceTo(target) <= 4.0F) {
                this.setAttackType(HybridAttackType.MELEE);
            } else {
                this.setAttackType(HybridAttackType.RANGED);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (dataAccessor.equals(ATTACK_TYPE)) {
            this.updateMainGoals();
        }
        super.onSyncedDataUpdated(dataAccessor);
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.define(TYPE, 0);
        this.entityData.define(ATTACK_TYPE, HybridAttackType.MELEE);
        super.defineSynchedData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putString("Variant", this.getVariant().getSerializedName());
        this.saveAttackType(nbt);
        super.addAdditionalSaveData(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.setVariant(FrostedZombieVariant.byName(nbt.getString("Variant")));
        this.loadAttackType(nbt);
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public FrostedZombieVariant getVariant() {
        return FrostedZombieVariant.byId(this.entityData.get(TYPE));
    }

    @Override
    public void setVariant(FrostedZombieVariant variant) {
        this.entityData.set(TYPE, variant.getId());
    }

    @Override
    public HybridAttackType getAttackType() {
        return this.entityData.get(ATTACK_TYPE);
    }

    @Override
    public void setAttackType(HybridAttackType type) {
        this.entityData.set(ATTACK_TYPE, type);
    }


    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!super.doHurtTarget(entity)) {
            return false;
        } else {
            if (entity instanceof LivingEntity living && entity.getType() != EntityType.IRON_GOLEM) {
                int mul = this.level.getLevelData().isHardcore() ? 2 : 1;
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100 * mul), this);
            }
            return true;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag nbt) {
        FrostedZombieVariant variant = this.getRandom().nextInt(100) > 65 ? FrostedZombieVariant.VAR_B : FrostedZombieVariant.VAR_A;
        this.setVariant(variant);
        return super.finalizeSpawn(accessor, instance, spawnType, groupData, nbt);
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float f0) {
        if (this.getAttackType() == 1) {
            FrostedSnowball frostedSnowball = new FrostedSnowball(this.level, this);

            double d0 = entity.getX() - this.getX();
            double d1 = entity.getY(0.3333333333333333D) - frostedSnowball.getY();
            double d2 = entity.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);

            frostedSnowball.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));

            this.playSound(HPSoundEvents.FROSTED_ZOMBIE_SHOOT.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(frostedSnowball);
        }
    }

    public void updateMainGoals() {
        if (this.isEffectiveAi()) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.rangedGoal);
            if (this.getAttackType() == HybridAttackType.RANGED) {
                this.goalSelector.addGoal(2, this.rangedGoal);
            } else {
                this.goalSelector.addGoal(2, this.meleeGoal);
            }
        }
    }
}
