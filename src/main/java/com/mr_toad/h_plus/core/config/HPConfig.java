package com.mr_toad.h_plus.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class HPConfig {

    public static ForgeConfigSpec COMMON;
    public static ForgeConfigSpec CLIENT;

    public static final String COMMON_SETTINGS = "common_settings";
    public static final String MOB_SETTINGS = "mob_settings";
    public static final String EVENT_SETTINGS = "event_settings";
    public static final String SPAWN_SETTINGS = "spawn_settings";
    public static final String CAN_CONVERSION_SETTINGS = "can_conversion_settings";

    public static final String CLIENT_SETTINGS = "client_settings";
    public static final String SCREEN_SETTINGS = "screen_settings";

    public static ForgeConfigSpec.BooleanValue canWitherSkeletonsSpawnWithBows;
    public static ForgeConfigSpec.BooleanValue canSpidersAttackSpawn;

    public static ForgeConfigSpec.BooleanValue allowHungryZombies;
    public static ForgeConfigSpec.BooleanValue canSkeletonsUseSpecialArrows;
    public static ForgeConfigSpec.BooleanValue canPillagerRetreat;

    public static ForgeConfigSpec.BooleanValue strongerMobsInDepths;
    public static ForgeConfigSpec.IntValue coordinateYNumber;

    public static ForgeConfigSpec.BooleanValue blockedLanPreset;

    public static ForgeConfigSpec.BooleanValue canMartyrSpawn;
    public static ForgeConfigSpec.BooleanValue canPutridSpawn;
    public static ForgeConfigSpec.BooleanValue canBonySandstoneSpawn;
    public static ForgeConfigSpec.BooleanValue canPolygonumSpawn;
    public static ForgeConfigSpec.BooleanValue canBabySpidersSpawn;
    public static ForgeConfigSpec.BooleanValue canSpiderSpawnBabies;

    public static ForgeConfigSpec.DoubleValue spidersBabyChance;
    public static ForgeConfigSpec.IntValue countOfSpawnsBabies;

    public static ForgeConfigSpec.BooleanValue skeleton2BonySandstone;
    public static ForgeConfigSpec.BooleanValue zombie2Martyr;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Common Settings").push(COMMON_SETTINGS);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Event Settings").push(EVENT_SETTINGS);
        COMMON_BUILDER.pop();
     
        allowHungryZombies = COMMON_BUILDER
                .comment(SuffixType.commentWithSuffix("Can zombies attack animals?", SuffixType.HARD))
                .define("allowHungryZombies", true);

        canSkeletonsUseSpecialArrows = COMMON_BUILDER
                .comment(SuffixType.commentWithSuffix("Can skeletons use the 'special' poisonous and harming arrows?", SuffixType.HARD))
                .define("canSkeletonsUseSpecialArrows", true);

        COMMON_BUILDER.comment("Mob Settings").push(MOB_SETTINGS);
        COMMON_BUILDER.pop();

        canPillagerRetreat = COMMON_BUILDER
                .comment(SuffixType.commentWithSuffix("Can pillagers retreat on reload", SuffixType.NORMAL_OR_HARDER))
                .define("canPillagerRetreat", true);

        canWitherSkeletonsSpawnWithBows = COMMON_BUILDER
                .comment(SuffixType.commentWithSuffix("Can wither skeletons spawn with bows?", SuffixType.HARD))
                .define("canWitherSkeletonsSpawnWithBows", true);

        canSpidersAttackSpawn = COMMON_BUILDER
                .comment(SuffixType.commentWithSuffix("Can spawn a horde of spiders / cave spiders? in caves", SuffixType.HARD))
                .define("canSpidersAttackSpawn", true);

        strongerMobsInDepths = COMMON_BUILDER
                .comment("Mobs become stronger on -y coord?")
                .define("strongerMobsOnNegativeY", true);

        coordinateYNumber = COMMON_BUILDER
                .comment(SuffixType.commentWithSuffix("Y coord, which started monster become stronger", SuffixType.HARD))
                .defineInRange("coordinateYNumber", -20, -64, 10);

        COMMON_BUILDER.comment("Spawn Settings").push(SPAWN_SETTINGS);
        COMMON_BUILDER.pop();

        canBonySandstoneSpawn = COMMON_BUILDER
                .comment("Can Bony Sandstone spawn?")
                .define("canBonySandstoneSpawn", true);

        canPolygonumSpawn = COMMON_BUILDER
                .comment("Can Polygonums spawn?")
                .define("canPolygonumSpawn", true);

        canMartyrSpawn = COMMON_BUILDER
                .comment("Can Martyrs spawn?")
                .define("canMartyrSpawn", true);

        canPutridSpawn = COMMON_BUILDER
                .comment("Can Putrids spawn?")
                .define("canPutridSpawn", true);

        canBabySpidersSpawn = COMMON_BUILDER
                .comment(SuffixType.commentWithSuffix("Can baby spiders spawn?", SuffixType.HARD))
                .define("canBabySpidersSpawn", true);

        spidersBabyChance = COMMON_BUILDER
               .comment("Chance that a spider (or subclass) is a baby. Allows changing the spider spawning mechanic.")
               .defineInRange("spidersBabyChance", 0.05D, 0.0D, 1.0D);

        canSpiderSpawnBabies = COMMON_BUILDER
               .comment(SuffixType.commentWithSuffix("Can adult spiders spawn babies?", SuffixType.HARD))
               .define("canSpiderSpawnBabies", true);

        countOfSpawnsBabies = COMMON_BUILDER
               .comment(SuffixType.commentWithSuffix("Count of baby spiders spawns from an adult spider | +2 if you in hardcore", SuffixType.HARD))
               .defineInRange("countOfSpawnsBabies", 4, 1, 20);

        COMMON_BUILDER.comment("Can Conversion settings").push(CAN_CONVERSION_SETTINGS);
        COMMON_BUILDER.pop();

        skeleton2BonySandstone = COMMON_BUILDER
                .comment("Can Skeleton Convert to Bony Sandstone? Convert in Quicksand block (Quicksand available in Storm++ Mod [Now not finished])")
                .define("canSkeletonConversionToBonySandstone", true);

        zombie2Martyr = COMMON_BUILDER
                .comment("Can Zombie Convert to Martyr? Convert in Powder Snow Block")
                .define("canMartyrConversionToBonySandstone", true);

        COMMON = COMMON_BUILDER.build();

        CLIENT_BUILDER.comment("Client Settings").push(CLIENT_SETTINGS);
        CLIENT_BUILDER.pop();
        CLIENT_BUILDER.comment("Screen Settings").push(SCREEN_SETTINGS);
        CLIENT_BUILDER.pop();

        blockedLanPreset = CLIENT_BUILDER
                .comment(SuffixType.commentWithSuffix("LAN Preset is Blocked? (GameType = Survival, Cheats = off)", SuffixType.HARDCORE))
                .define("blockedLanPreset", true);

        CLIENT = CLIENT_BUILDER.build();
    }



}

