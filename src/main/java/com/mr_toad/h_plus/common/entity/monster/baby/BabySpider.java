package com.mr_toad.h_plus.common.entity.monster.baby;

import com.mr_toad.h_plus.common.entity.monster.ai.goal.BabySpidersFollowParentGoal;
import com.mr_toad.h_plus.common.util.entitydata.SpiderSpawnsDataContainer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BabySpider extends Spider implements SpiderSpawnsDataContainer {

    public BabySpider(EntityType<? extends Spider> ets, Level lvl) {
        super(ets, lvl);
    }

    public static AttributeSupplier.Builder createBSAttributes() {
        return Spider.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.45D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new BabySpidersFollowParentGoal<>(this, Spider.class));
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
        return 0.325F;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ForgeSpawnEggItem.fromEntityType(EntityType.SPIDER));
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
