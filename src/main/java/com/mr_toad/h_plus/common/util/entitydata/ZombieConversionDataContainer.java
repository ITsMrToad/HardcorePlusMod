package com.mr_toad.h_plus.common.util.entitydata;

import com.mr_toad.lib.api.entity.entitydata.EntityDataContainer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Zombie;

public interface ZombieConversionDataContainer extends EntityDataContainer {

    EntityDataAccessor<Boolean> MARTYR_CONVERSION = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);

    default boolean isFreezeConversion() {
        return this.getData().get(MARTYR_CONVERSION);
    }

    default boolean isShaking() {
        return this.isFreezeConversion();
    }

    default void setFreezeConversion(boolean aB) {
        this.getData().set(MARTYR_CONVERSION, aB);
    }
}
