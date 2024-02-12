package com.mr_toad.h_plus.common.entity.monster;

import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.core.config.HPConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class JungleSkeleton extends AbstractSkeleton {

    public JungleSkeleton(EntityType<? extends AbstractSkeleton> etas, Level lvl) {
        super(etas, lvl);
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    protected AbstractArrow getArrow(ItemStack stack, float f0) {
        AbstractArrow abstractarrow = super.getArrow(stack, f0);
        if (abstractarrow instanceof Arrow) {
            ((Arrow)abstractarrow).addEffect(new MobEffectInstance(MobEffects.POISON, 250));
        }

        return abstractarrow;
    }


    public static boolean checkJSSpawnRules(EntityType<? extends Monster> et, ServerLevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource rand) {
        return HPMiscUtils.baseCheckEntitySpawnRules(et, accessor, type, pos, rand, HPConfig.canPolygonumSpawn.get());
    }

}
