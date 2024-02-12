package com.mr_toad.h_plus.common.entity.monster;

import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.core.config.HPConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

@MethodsReturnNonnullByDefault
public class DesertSkeleton extends AbstractSkeleton {

    public DesertSkeleton(EntityType<? extends AbstractSkeleton> etas, Level lvl) {
        super(etas, lvl);
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
