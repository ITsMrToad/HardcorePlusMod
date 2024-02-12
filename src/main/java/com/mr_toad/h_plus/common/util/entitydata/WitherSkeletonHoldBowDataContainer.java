package com.mr_toad.h_plus.common.util.entitydata;

import com.mr_toad.lib.api.entity.entitydata.EntityDataContainer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.WitherSkeleton;

public interface WitherSkeletonHoldBowDataContainer extends EntityDataContainer {

    EntityDataAccessor<Boolean> CAN_HOLD_BOW = SynchedEntityData.defineId(WitherSkeleton.class, EntityDataSerializers.BOOLEAN);

    default boolean canHoldBow() {
        return this.getData().get(CAN_HOLD_BOW);
    }

    default void setCanHoldBow(boolean aB) {
        this.getData().set(CAN_HOLD_BOW, aB);
    }

    default void defineWitherSkeletonHoldBowData() {
        this.getData().define(CAN_HOLD_BOW, false);
    }

}
