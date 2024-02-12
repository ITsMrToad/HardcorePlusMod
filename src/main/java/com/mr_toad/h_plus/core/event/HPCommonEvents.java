package com.mr_toad.h_plus.core.event;

import com.mr_toad.h_plus.common.entity.illager.ai.goal.PillagerRetreatGoal;
import com.mr_toad.h_plus.common.entity.monster.ai.goal.SpiderSpawnBabiesGoal;
import com.mr_toad.h_plus.common.entity.monster.ai.goal.ZombieStealHPGoal;
import com.mr_toad.h_plus.common.level.gen.spawner.SpidersAttack;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.common.util.entitydata.SpiderSpawnsDataContainer;
import com.mr_toad.h_plus.core.HPlus;
import com.mr_toad.h_plus.core.config.HPConfig;
import com.mr_toad.lib.api.entity.ai.goal.AttackTargetIfAccessedGoal;
import com.mr_toad.lib.api.util.DifficultyPredicates;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HPlus.MODID)
public class HPCommonEvents {

    public static final SpidersAttack SPIDERS_ATTACK = new SpidersAttack();

    @SubscribeEvent
    public static void onBaseAttributeModification(EntityAttributeModificationEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        if (level == null) return;
        if (DifficultyPredicates.isHard(level)) {
            event.add(EntityType.PIGLIN, Attributes.MAX_HEALTH, 25.0F);
            event.add(EntityType.DROWNED, Attributes.FOLLOW_RANGE, 40.0D);
            if (level.getLevelData().isHardcore()) {
                event.add(EntityType.BLAZE, Attributes.MAX_HEALTH, 25.0F);
            }

        }
    }

    @SubscribeEvent
    public static void onTickLevel(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel serverLevel && serverLevel.dimension() == Level.OVERWORLD) {
            if (HPConfig.canSpidersAttackSpawn.get()) {
                SPIDERS_ATTACK.tick(serverLevel, true, false);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityLoad(EntityJoinLevelEvent event) {
        Level level = event.getLevel();
        if (level.dimension() == Level.OVERWORLD && !level.isClientSide()) {
            HPMiscUtils.entityStatsChange(event.getEntity());
        }

        if (event.getEntity() instanceof Pillager pillager && HPConfig.canPillagerRetreat.get()) {
            pillager.goalSelector.addGoal(1, new PillagerRetreatGoal(pillager));
        } else if (event.getEntity() instanceof Zombie zombie && HPConfig.allowHungryZombies.get()) {
            ZombieStealHPGoal stealHPGoal = new ZombieStealHPGoal(zombie);
            zombie.goalSelector.addGoal(2, stealHPGoal);
            zombie.targetSelector.addGoal(5, new AttackTargetIfAccessedGoal<>(zombie, Animal.class, stealHPGoal));
        } else if (event.getEntity() instanceof Spider spider) {
            SpiderSpawnsDataContainer dataContainer = (SpiderSpawnsDataContainer) spider;
            spider.goalSelector.addGoal(5, new SpiderSpawnBabiesGoal(spider, dataContainer));
        }
    }

    @SubscribeEvent
    public static void finalizeSpidersSpawn(MobSpawnEvent.FinalizeSpawn event) {
        boolean hardcore = event.getEntity().level.getLevelData().isHardcore();
        int i = event.getEntity().getRandom().nextInt(2);
        int j = hardcore ? 2 : 0;
        int k = event.getEntity().getRandom().nextInt(100);

        if (event.getEntity() instanceof Spider spider) {
            SpiderSpawnsDataContainer spiderSpawnsDataContainer = (SpiderSpawnsDataContainer) spider;
            spiderSpawnsDataContainer.setCanSpawn(k > 150);
            if (spiderSpawnsDataContainer.canSpawn()) {
                spiderSpawnsDataContainer.setSpidersCount(i + j + HPConfig.countOfSpawnsBabies.get());
            }
        }

        if (event.getEntity() instanceof CaveSpider caveSpider) {
            SpiderSpawnsDataContainer caveSpiderData = (SpiderSpawnsDataContainer) caveSpider;
            caveSpiderData.setCanSpawn(k > 100);
            if (caveSpiderData.canSpawn()) {
                caveSpiderData.setSpidersCount(i + j + HPConfig.countOfSpawnsBabies.get());
            }
        }
    }

    @SubscribeEvent
    public static void replaceSpiderWithBabyVersion(LivingPackSizeEvent event) {
        if (!HPConfig.canBabySpidersSpawn.get()) {
            event.setCanceled(true);
            return;
        }

        Entity mob = event.getEntity();
        RandomSource rand = mob.level.getRandom();
        if (!(mob.level instanceof ServerLevel && (rand.nextFloat() < HPConfig.spidersBabyChance.get()) && DifficultyPredicates.isHard(mob.level) && HPConfig.canSpiderSpawnBabies.get())) return;

        EntityType<? extends Spider> babyType = HPMiscUtils.SPIDER_BY_SPIDER_RELATION.get(mob.getType());

        if (babyType == null) return;
        Mob babyMob = babyType.create(mob.level);
        if (babyMob == null) return;

        babyMob.moveTo(mob.getX(), mob.getY(), mob.getZ(), Mth.wrapDegrees(babyMob.level.random.nextFloat() * 360.0F), 0.0F);
        ((ServerLevel) mob.level).addFreshEntityWithPassengers(babyMob);
        babyMob.yHeadRot = babyMob.getYRot();
        babyMob.yBodyRot = babyMob.getYRot();
        ForgeEventFactory.onFinalizeSpawn(babyMob, (ServerLevel) babyMob.level, babyMob.level.getCurrentDifficultyAt(babyMob.blockPosition()), MobSpawnType.NATURAL, null, null);
        int j = mob.level.getLevelData().isHardcore() ? 50 : 75;
        if (rand.nextInt() > j) {
            HPMiscUtils.setBabyRandomEffect(rand, babyMob);
        }
        mob.discard();
    }


}
