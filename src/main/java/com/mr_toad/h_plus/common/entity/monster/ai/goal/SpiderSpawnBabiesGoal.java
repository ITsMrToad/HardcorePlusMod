package com.mr_toad.h_plus.common.entity.monster.ai.goal;

import com.mr_toad.h_plus.common.util.entitydata.SpiderSpawnsDataContainer;
import com.mr_toad.h_plus.core.config.HPConfig;
import com.mr_toad.h_plus.core.init.HPEntityType;
import com.mr_toad.lib.api.util.DifficultyPredicates;
import com.mr_toad.lib.api.util.time.Disposable;
import com.mr_toad.lib.api.util.time.IntegerCooldown;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;

public class SpiderSpawnBabiesGoal extends Goal {

    private final Monster mob;
    private final SpiderSpawnsDataContainer dataContainer;

    private final Disposable disposable = Disposable.createNull(this.getClass().getSimpleName() + ":Disposable");
    private final IntegerCooldown cooldownTickReq = new IntegerCooldown(40, "SpiderSpawnBabiesReqCooldown").setImmutable();

    public SpiderSpawnBabiesGoal(Monster mob, SpiderSpawnsDataContainer dataContainer) {
        this.mob = mob;
        this.dataContainer = dataContainer;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isAlive() && DifficultyPredicates.isHard(this.mob.level) && !this.mob.isVehicle()) {
            if (HPConfig.canSpiderSpawnBabies.get()) {
                if (this.cooldownTickReq.getCooldown() > 0) {
                    this.cooldownTickReq.tickDown();
                    return false;
                } else {
                    return this.disposable.canUse() && this.dataContainer.canSpawn();
                }
            }
        }
        return false;
    }


    @Override
    public void start() {
        super.start();
        if (!this.dataContainer.canSpawn()) return;
        ServerLevel serverlevel = (ServerLevel) this.mob.level;
        int count = this.dataContainer.getSpidersCount();
        this.dataContainer.setSpidersCount(count);
        for (int k = 0; k < 4; ++k) {
            RandomSource rand = this.mob.getRandom();
            BlockPos blockpos = this.getPos(rand);
            DifficultyInstance instance = this.mob.level.getCurrentDifficultyAt(blockpos);
            Spider spider = HPEntityType.BABY_SPIDER.get().create(this.mob.level);
            if (this.mob instanceof CaveSpider caveSpider) {
                spider = HPEntityType.BABY_CAVE_SPIDER.get().create(caveSpider.level);
            }
            if (spider != null) {
                spider.moveTo(blockpos, 0.0F, 0.0F);
                ForgeEventFactory.onFinalizeSpawn(spider, (ServerLevelAccessor) this.mob.level, instance, MobSpawnType.MOB_SUMMONED, null, null);
                serverlevel.addFreshEntityWithPassengers(spider);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.dataContainer.setCanSpawn(false);
        this.disposable.setUsed();
    }

    public BlockPos getPos(RandomSource source) {
        return this.mob.blockPosition().offset(-2 + source.nextInt(3), 1, -2 + source.nextInt(3));
    }
}
