package com.mr_toad.h_plus.common.entity.monster.baby;

import com.mr_toad.h_plus.common.entity.monster.ai.goal.BabySpidersFollowParentGoal;
import com.mr_toad.h_plus.common.util.entitydata.SpiderSpawnsDataContainer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BabyCaveSpider extends CaveSpider implements SpiderSpawnsDataContainer {

    public BabyCaveSpider(EntityType<? extends CaveSpider> ets, Level lvl) {
        super(ets, lvl);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new BabySpidersFollowParentGoal<>(this, CaveSpider.class));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
    }

    @Override
    protected boolean canAddPassenger(Entity entity) {
        return false;
    }

    @Override
    public boolean isBaby() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return 0.295F;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ForgeSpawnEggItem.fromEntityType(EntityType.CAVE_SPIDER));
    }

    @Override
    public int getSpidersCount() {
        return 0;
    }

    @Override
    public boolean canSpawn() {
        return false;
    }

    @Override
    public @NotNull SynchedEntityData getData() {
        return this.entityData;
    }
}
