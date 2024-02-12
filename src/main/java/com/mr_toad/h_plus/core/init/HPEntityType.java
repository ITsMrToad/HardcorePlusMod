package com.mr_toad.h_plus.core.init;

import com.mr_toad.h_plus.common.entity.monster.DesertSkeleton;
import com.mr_toad.h_plus.common.entity.monster.FrostedZombie;
import com.mr_toad.h_plus.common.entity.monster.JungleSkeleton;
import com.mr_toad.h_plus.common.entity.monster.JungleZombie;
import com.mr_toad.h_plus.common.entity.monster.baby.BabyCaveSpider;
import com.mr_toad.h_plus.common.entity.monster.baby.BabySpider;
import com.mr_toad.h_plus.common.entity.projectile.FrostedSnowball;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.core.HPlus;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = HPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HPEntityType {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HPlus.MODID);

    public static final RegistryObject<EntityType<FrostedSnowball>> FROSTED_SNOWBALL = ENTITIES.register("frosted_snowball", () -> EntityType.Builder.<FrostedSnowball>of(FrostedSnowball::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(HPMiscUtils.makeRsAsString("frosted_snowball")));

    public static final RegistryObject<EntityType<JungleZombie>> JUNGLE_ZOMBIE = ENTITIES.register("jungle_zombie", () -> EntityType.Builder.of(JungleZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(HPMiscUtils.makeRsAsString("jungle_zombie")));
    public static final RegistryObject<EntityType<FrostedZombie>> FROSTED_ZOMBIE = ENTITIES.register("frosted_zombie", () -> EntityType.Builder.of(FrostedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(HPMiscUtils.makeRsAsString("frosted_zombie")));

    public static final RegistryObject<EntityType<DesertSkeleton>> DESERT_SKELETON = ENTITIES.register("desert_skeleton", () -> EntityType.Builder.of(DesertSkeleton::new, MobCategory.MONSTER).sized(0.7F, 2.4F).clientTrackingRange(8).build(HPMiscUtils.makeRsAsString("desert_skeleton")));
    public static final RegistryObject<EntityType<JungleSkeleton>> JUNGLE_SKELETON = ENTITIES.register("jungle_skeleton", () -> EntityType.Builder.of(JungleSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(HPMiscUtils.makeRsAsString("jungle_skeleton")));

    public static final RegistryObject<EntityType<BabySpider>> BABY_SPIDER = ENTITIES.register("baby_spider", () -> EntityType.Builder.of(BabySpider::new, MobCategory.MONSTER).sized(0.7F, 0.4F).clientTrackingRange(8).build(HPMiscUtils.makeRsAsString("baby_spider")));
    public static final RegistryObject<EntityType<BabyCaveSpider>> BABY_CAVE_SPIDER = ENTITIES.register("baby_cave_spider", () -> EntityType.Builder.of(BabyCaveSpider::new, MobCategory.MONSTER).sized(0.6F, 0.35F).clientTrackingRange(8).build(HPMiscUtils.makeRsAsString("baby_cave_spider")));

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(HPEntityType.JUNGLE_ZOMBIE.get(), JungleZombie.createAttributes().build());
        event.put(HPEntityType.FROSTED_ZOMBIE.get(), FrostedZombie.createFrostedZombieAttributes().build());
        event.put(HPEntityType.DESERT_SKELETON.get(), DesertSkeleton.createDSAttributes().build());
        event.put(HPEntityType.JUNGLE_SKELETON.get(), JungleSkeleton.createAttributes().build());

        event.put(HPEntityType.BABY_SPIDER.get(), BabySpider.createBSAttributes().build());
        event.put(HPEntityType.BABY_CAVE_SPIDER.get(), BabySpider.createBSAttributes().build());
    }

}
