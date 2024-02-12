package com.mr_toad.h_plus.common.level.gen.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.common.Tags;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpidersAttack implements CustomSpawner {

    public int tickCount;

    @Override
    @SuppressWarnings("deprecation")
    public int tick(ServerLevel serverLevel, boolean b0, boolean b1) {
        if (b0 && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            --this.tickCount;
            if (this.tickCount <= 0) {
                this.tickCount = 1200;
                RandomSource rand = serverLevel.getRandom();
                Player player = serverLevel.getRandomPlayer();
                if (player != null && serverLevel.isNight()) {

                    int x = (8 + rand.nextInt(26)) * (rand.nextBoolean() ? -1 : 1);
                    int y = (4 + rand.nextInt(14)) * (rand.nextBoolean() ? -1 : 1);
                    int z = (8 + rand.nextInt(28)) * (rand.nextBoolean() ? -1 : 1);

                    int k = 10;

                    BlockPos blockpos = player.blockPosition().offset(x, y, z);
                    if (serverLevel.hasChunksAt(blockpos.getX() - k, blockpos.getZ() - k, blockpos.getX() + k, blockpos.getZ() + k)) {
                        boolean entityFlag = NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, serverLevel, blockpos, EntityType.SPIDER) || NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, serverLevel, blockpos, EntityType.CAVE_SPIDER);
                        if (entityFlag) {
                            boolean canSpawnCave = rand.nextInt(10) == 0 && player.blockPosition().getY() < 10;
                            this.spawnInCaves(serverLevel, blockpos, canSpawnCave);
                        }
                    }
                }
            }

        }
        return 0;
    }

    private void spawnInCaves(ServerLevel serverLevel, BlockPos blockPos, boolean isCave) {
        int calcValue = 2;
        int randomized = serverLevel.getRandom().nextInt(10) + calcValue;
        if (serverLevel.getBiome(blockPos).is(Tags.Biomes.IS_CAVE)) {
            for (int i = 0; i < randomized; ++i) {
                this.spawnSpiders(blockPos, serverLevel, isCave);
            }
        }
    }

    private void spawnSpiders(BlockPos blockPos, ServerLevel serverLevel, boolean isCave) {
        Spider spider = isCave ? EntityType.CAVE_SPIDER.create(serverLevel) : EntityType.SPIDER.create(serverLevel);
        if (spider == null) return;

        if (serverLevel.getBiome(spider.blockPosition()).is(Tags.Biomes.IS_CAVE)) {
            spider.moveTo(blockPos, 0.0F, 0.0F);
            spider.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(blockPos), MobSpawnType.NATURAL, (SpawnGroupData) null, (CompoundTag) null);
            serverLevel.addFreshEntityWithPassengers(spider);
        }
    }
}
