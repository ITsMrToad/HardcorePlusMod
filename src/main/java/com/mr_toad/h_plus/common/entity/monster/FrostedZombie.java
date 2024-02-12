package com.mr_toad.h_plus.common.entity.monster;

import com.mr_toad.h_plus.common.entity.monster.variant.FrostedZombieVariant;
import com.mr_toad.h_plus.common.entity.projectile.FrostedSnowball;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.core.config.HPConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FrostedZombie extends Zombie implements VariantHolder<FrostedZombieVariant>, RangedAttackMob {

    private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(FrostedZombie.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> ATTACK_TYPE = SynchedEntityData.defineId(FrostedZombie.class, EntityDataSerializers.BYTE);

    private final ZombieAttackGoal zombieAttackGoal = new ZombieAttackGoal(this, 1.0D, false);
    private final RangedAttackGoal rangedAttackGoal = new RangedAttackGoal(this, 1.25D, 20, 10.0F);

    public FrostedZombie(EntityType<? extends Zombie> etz, Level lvl) {
        super(etz, lvl);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
        this.reassessGoals();
    }

    public static AttributeSupplier.Builder createFrostedZombieAttributes() {
        return Zombie.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.18D);
    }

    public static boolean checkFZSpawnRules(EntityType<? extends Monster> et, ServerLevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return HPMiscUtils.baseCheckEntitySpawnRules(et, accessor, type, pos, rand, HPConfig.canMartyrSpawn.get());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ATTACK_TYPE, (byte) 0);
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
        this.setVariant(FrostedZombieVariant.byName(nbt.getString("Variant")));
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

    public byte getAttackType() {
        return this.entityData.get(ATTACK_TYPE);
    }

    public void setAttackType(byte type) {
        this.entityData.define(ATTACK_TYPE, type);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!super.doHurtTarget(entity)) {
            return false;
        } else {
            if (entity instanceof LivingEntity living && entity.getType() != EntityType.IRON_GOLEM) {
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300), this);
            }
            return true;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag nbt) {
        int i = this.getRandom().nextInt(100);
        int j = this.getRandom().nextInt(50);
        FrostedZombieVariant variant = i > 50 ? FrostedZombieVariant.VAR_B : FrostedZombieVariant.VAR_A;
        byte attackType = (byte) (j > 25 ? 1 : 0);
        this.setAttackType(attackType);
        this.setVariant(variant);
        return super.finalizeSpawn(accessor, instance, spawnType, groupData, nbt);
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float f0) {
        if (this.getAttackType() == 1) {
            FrostedSnowball snowball = new FrostedSnowball(this.level, this);

            double d0 = entity.getEyeY() - (double) 1.1F;
            double d1 = entity.getX() - this.getX();
            double d2 = d0 - snowball.getY();
            double d3 = entity.getZ() - this.getZ();
            double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double) 0.2F;

            snowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);

            this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(snowball);
        }
    }

    public void reassessGoals() {
        if (this.getAttackType() == 0) {
            this.goalSelector.addGoal(1, this.zombieAttackGoal);
            this.goalSelector.removeGoal(this.rangedAttackGoal);
        } else if (this.getAttackType() == 1) {
            this.goalSelector.addGoal(1, this.rangedAttackGoal);
            this.goalSelector.removeGoal(this.zombieAttackGoal);
        }
    }
}