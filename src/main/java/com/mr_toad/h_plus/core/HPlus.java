package com.mr_toad.h_plus.core;

import com.mojang.logging.LogUtils;
import com.mr_toad.h_plus.client.model.geom.HPModelLayers;
import com.mr_toad.h_plus.client.renderer.BabyCaveSpiderRenderer;
import com.mr_toad.h_plus.client.renderer.BabySpiderRenderer;
import com.mr_toad.h_plus.client.renderer.DesertSkeletonRenderer;
import com.mr_toad.h_plus.client.renderer.FrostedZombieRenderer;
import com.mr_toad.h_plus.client.renderer.JungleSkeletonRenderer;
import com.mr_toad.h_plus.client.renderer.JungleZombieRenderer;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import com.mr_toad.h_plus.core.config.HPConfig;
import com.mr_toad.h_plus.core.init.HPEntityType;
import com.mr_toad.h_plus.core.init.HPItems;
import com.mr_toad.h_plus.core.init.HPSoundEvents;
import com.mr_toad.h_plus.core.message.MessageC2SPlayerDied;
import com.mr_toad.lib.event.ToadEventFactory;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;


@Mod(HPlus.MODID)
public class HPlus {

    public static final String MODID = "h_plus";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CubeDeformation OUTER_ARMOR_DEFORMATION = new CubeDeformation(1.0F);
    private static final CubeDeformation INNER_ARMOR_DEFORMATION = new CubeDeformation(0.5F);
    private static final CubeDeformation LAYER = new CubeDeformation(0.25F);

    private static final LayerDefinition OUTER = LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_ARMOR_DEFORMATION), 64, 32);
    private static final LayerDefinition INNER = LayerDefinition.create(HumanoidArmorModel.createBodyLayer(INNER_ARMOR_DEFORMATION), 64, 32);

    public static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "net"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
  
    public HPlus() {
        final ModLoadingContext ctx = ModLoadingContext.get();
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        HPEntityType.ENTITIES.register(bus);
        HPItems.ITEMS.register(bus);
        HPSoundEvents.SOUNDS.register(bus);

        bus.addListener(this::commonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(this::registerRenderers);
            bus.addListener(this::registerLayerDefinitions);
        });

        ctx.registerConfig(ModConfig.Type.COMMON, HPConfig.COMMON);
        ctx.registerConfig(ModConfig.Type.CLIENT, HPConfig.CLIENT);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CHANNEL.messageBuilder(MessageC2SPlayerDied.class, 0, NetworkDirection.PLAY_TO_CLIENT).encoder(MessageC2SPlayerDied::write).decoder(MessageC2SPlayerDied::read).consumerMainThread(MessageC2SPlayerDied::handle).add();
        event.enqueueWork(HPMiscUtils::registerSBSR);
    }

    @SubscribeEvent
    public static void registerSpawns(SpawnPlacementRegisterEvent event) {
        event.register(HPEntityType.DESERT_SKELETON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DesertSkeleton::checkDSSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(HPEntityType.JUNGLE_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, JungleZombie::checkJZSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(HPEntityType.JUNGLE_SKELETON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, JungleSkeleton::checkJSSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(HPEntityType.FROSTED_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FrostedZombie::checkFZSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }


    @OnlyIn(Dist.CLIENT)
    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(HPEntityType.DESERT_SKELETON.get(), DesertSkeletonRenderer::new);
        event.registerEntityRenderer(HPEntityType.JUNGLE_ZOMBIE.get(), JungleZombieRenderer::new);
        event.registerEntityRenderer(HPEntityType.FROSTED_ZOMBIE.get(), FrostedZombieRenderer::new);
        event.registerEntityRenderer(HPEntityType.JUNGLE_SKELETON.get(), JungleSkeletonRenderer::new);

        event.registerEntityRenderer(HPEntityType.BABY_SPIDER.get(), BabySpiderRenderer::new);
        event.registerEntityRenderer(HPEntityType.BABY_CAVE_SPIDER.get(), BabyCaveSpiderRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HPModelLayers.FROSTED_ZOMBIE, () -> HPMiscUtils.createZombieBodyLayer(CubeDeformation.NONE));
        event.registerLayerDefinition(HPModelLayers.JUNGLE_ZOMBIE, () -> HPMiscUtils.createZombieBodyLayer(CubeDeformation.NONE));
        event.registerLayerDefinition(HPModelLayers.DESERT_SKELETON, SkeletonModel::createBodyLayer);
        event.registerLayerDefinition(HPModelLayers.JUNGLE_SKELETON, SkeletonModel::createBodyLayer);

        event.registerLayerDefinition(HPModelLayers.FROSTED_ZOMBIE_INNER_ARMOR, () -> INNER);
        event.registerLayerDefinition(HPModelLayers.JUNGLE_ZOMBIE_INNER_ARMOR, () -> INNER);
        event.registerLayerDefinition(HPModelLayers.DESERT_SKELETON_INNER_ARMOR, () -> INNER);
        event.registerLayerDefinition(HPModelLayers.JUNGLE_SKELETON_INNER_ARMOR, () -> INNER);

        event.registerLayerDefinition(HPModelLayers.FROSTED_ZOMBIE_OUTER_ARMOR, () -> OUTER);
        event.registerLayerDefinition(HPModelLayers.JUNGLE_ZOMBIE_OUTER_ARMOR, () -> OUTER);
        event.registerLayerDefinition(HPModelLayers.DESERT_SKELETON_OUTER_ARMOR, () -> OUTER);
        event.registerLayerDefinition(HPModelLayers.JUNGLE_SKELETON_OUTER_ARMOR, () -> OUTER);

        event.registerLayerDefinition(HPModelLayers.ZOMBIE_ICE, () -> HPMiscUtils.createZombieBodyLayer(LAYER));
        event.registerLayerDefinition(HPModelLayers.ZOMBIE_MOSS, () -> HPMiscUtils.createZombieBodyLayer(LAYER));
    }

}
