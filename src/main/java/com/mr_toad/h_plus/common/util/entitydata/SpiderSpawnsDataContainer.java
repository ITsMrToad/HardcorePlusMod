package com.mr_toad.h_plus.common.util.entitydata;

import com.mr_toad.lib.api.entity.entitydata.EntityDataContainer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Spider;

public interface SpiderSpawnsDataContainer extends EntityDataContainer {

    EntityDataAccessor<Boolean> CAN_SPAWN = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BOOLEAN);
    EntityDataAccessor<Integer> SPIDERS_COUNT = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.INT);

    default int getSpidersCount() {
        return this.getData().get(SPIDERS_COUNT);
    }

    default boolean canSpawn() {
        return this.getData().get(CAN_SPAWN);
    }

    default void setCanSpawn(boolean aB) {
        this.getData().set(CAN_SPAWN, aB);
    }

    default void setSpidersCount(int count) {
        this.getData().set(SPIDERS_COUNT, count);
    }

    default void defineSpidersSpawnData() {
        this.getData().define(CAN_SPAWN, false);
        this.getData().define(SPIDERS_COUNT, 0);
    }

}
