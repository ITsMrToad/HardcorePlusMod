package com.mr_toad.h_plus.common.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mr_toad.h_plus.api.EntityDepthStatsChangeEvent;
import com.mr_toad.h_plus.core.HPlus;
import com.mr_toad.h_plus.core.config.HPConfig;
import com.mr_toad.h_plus.core.init.HPEntityType;
import com.mr_toad.lib.api.util.DifficultyPredicates;
import com.mr_toad.lib.core.data.tag.ToadlyTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class HPMiscUtils {

    private static final UUID FOLLOW_RANGE_BOOST_UUID = UUID.fromString("cd20f11c-f086-43ab-b1ec-5552c6e37206");
    private static final UUID ARMOR_BOOST_UUID = UUID.fromString("255238ea-2480-47e3-885b-4547091681d6");
    private static final UUID SKELETON_ARMOR_BOOST_UUID = UUID.fromString("bf1639bd-1040-474b-a077-38f618866435");
    private static final UUID ZOMBIE_HEALTH_BOOST_UUID = UUID.fromString("777567d1-49e9-4dec-a3f5-a94b23dad61b");
    private static final UUID ARTHROPOD_DAMAGE_BOOST_UUID = UUID.fromString("d729bc32-aebb-436e-82e9-e177ebbf6df8");

    public static final BiMap<EntityType<? extends Spider>, EntityType<? extends Spider>> SPIDER_BY_SPIDER_RELATION = HashBiMap.create();

    @Nullable public static MobEffect effect;

    public static void entityStatsChange(Entity entity) {
        if (HPConfig.strongerMobsInDepths.get()) {
            List<? extends Player> list = entity.level.players();
            if (list.isEmpty()) return;
            for (Player player : list) {
                if (entity.blockPosition().getY() < HPConfig.coordinateYNumber.get() && player.blockPosition().getY() < HPConfig.coordinateYNumber.get()) {
                    if (entity instanceof Monster getter) {
                        if (getter.isAlive() && !(getter.getType().is(ToadlyTags.ToadlyEntityTypeTags.NETHER_MOBS)) || getter.getType().is(ToadlyTags.ToadlyEntityTypeTags.END_MOBS) && !(getter.isVehicle() || getter.isPassenger())) {
                            AttributeInstance followRangeInstance = getter.getAttribute(Attributes.FOLLOW_RANGE);
                            AttributeInstance armorInstance = getter.getAttribute(Attributes.ARMOR);

                            if (followRangeInstance == null) return;
                            if (armorInstance == null) return;

                            followRangeInstance.addTransientModifier(new AttributeModifier(FOLLOW_RANGE_BOOST_UUID, "Follow range boost for depth mobs", 15.0D, AttributeModifier.Operation.ADDITION));
                            armorInstance.addTransientModifier(new AttributeModifier(ARMOR_BOOST_UUID, "Armor boost for depth mobs", 1.0D, AttributeModifier.Operation.ADDITION));

                            if (getter.getType() == EntityType.SKELETON) {
                                AttributeInstance armorInstanceSkeleton = getter.getAttribute(Attributes.ARMOR);
                                if (armorInstanceSkeleton == null) return;
                                double armorModifier = getter.isHolding(itemStack -> itemStack.is(ItemTags.SWORDS)) ? 2.0D : 1.0D;
                                armorInstanceSkeleton.addTransientModifier(new AttributeModifier(SKELETON_ARMOR_BOOST_UUID, "Armor boost for depth skeletons", armorModifier, AttributeModifier.Operation.ADDITION));
                            } else if (getter.getType() == EntityType.ZOMBIE) {
                                AttributeInstance healthInstance = getter.getAttribute(Attributes.MAX_HEALTH);
                                if (healthInstance == null) return;
                                healthInstance.addTransientModifier(new AttributeModifier(ZOMBIE_HEALTH_BOOST_UUID, "Health boost for depth zombies", 5.0F, AttributeModifier.Operation.ADDITION));
                            } else if (getter.getType() == EntityType.SPIDER || getter.getType() == EntityType.CAVE_SPIDER || getter.getType() == EntityType.SILVERFISH) {
                                AttributeInstance damageInstance = getter.getAttribute(Attributes.ATTACK_DAMAGE);
                                if (damageInstance == null) return;
                                damageInstance.addTransientModifier(new AttributeModifier(ARTHROPOD_DAMAGE_BOOST_UUID, "Damage boost for depth arthropods", 2.0F, AttributeModifier.Operation.ADDITION));
                            }

                            MinecraftForge.EVENT_BUS.post(new EntityDepthStatsChangeEvent(getter));
                        }
                    }
                }
            }
        }
    }

    public static String makeRsAsString(String name) {
        return HPlus.MODID + ":" + name;
    }

    public static ResourceLocation makeRs(String name) {
        return new ResourceLocation(HPlus.MODID, name);
    }

    public static LayerDefinition createZombieBodyLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(1.9F, 12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static boolean baseCheckEntitySpawnRules(EntityType<? extends Monster> et, ServerLevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource rand, boolean configValue) {
        return Mob.checkMobSpawnRules(et, accessor, type, pos, rand) && configValue && DifficultyPredicates.isHard(accessor.getLevel());
    }

    public static void setBabyRandomEffect(RandomSource source, Mob mob) {
        int i = source.nextInt(3);
        if (i <= 1) {
            effect = MobEffects.REGENERATION;
        } else if (i == 2) {
            effect = MobEffects.INVISIBILITY;
        }
        int j = mob.level.getLevelData().isHardcore() ? 400 : 300;
        if (effect != null) {
            mob.addEffect(new MobEffectInstance(effect, j));
        }
    }

    public static void registerSBSR() {
        SPIDER_BY_SPIDER_RELATION.put(EntityType.SPIDER, HPEntityType.BABY_SPIDER.get());
        SPIDER_BY_SPIDER_RELATION.put(EntityType.CAVE_SPIDER, HPEntityType.BABY_CAVE_SPIDER.get());
    }
}
