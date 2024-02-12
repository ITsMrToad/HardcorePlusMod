package com.mr_toad.h_plus.client.model.geom;

import com.mr_toad.h_plus.core.HPlus;
import com.mr_toad.lib.api.client.ToadClientUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HPModelLayers {

    public static final ModelLayerLocation FROSTED_ZOMBIE = register("frosted_zombie");
    public static final ModelLayerLocation FROSTED_ZOMBIE_INNER_ARMOR = registerInnerArmor("frosted_zombie");
    public static final ModelLayerLocation FROSTED_ZOMBIE_OUTER_ARMOR = registerOuterArmor("frosted_zombie");
    public static final ModelLayerLocation ZOMBIE_ICE = register("zombie_ice");

    public static final ModelLayerLocation JUNGLE_ZOMBIE = register("jungle_zombie");
    public static final ModelLayerLocation JUNGLE_ZOMBIE_INNER_ARMOR = registerInnerArmor("jungle_zombie");
    public static final ModelLayerLocation JUNGLE_ZOMBIE_OUTER_ARMOR = registerOuterArmor("jungle_zombie");
    public static final ModelLayerLocation ZOMBIE_MOSS = register("zombie_moss");

    public static final ModelLayerLocation DESERT_SKELETON = register("desert_skeleton");
    public static final ModelLayerLocation DESERT_SKELETON_INNER_ARMOR = registerInnerArmor("desert_skeleton");
    public static final ModelLayerLocation DESERT_SKELETON_OUTER_ARMOR = registerOuterArmor("desert_skeleton");

    public static final ModelLayerLocation JUNGLE_SKELETON = register("jungle_skeleton");
    public static final ModelLayerLocation JUNGLE_SKELETON_INNER_ARMOR = registerInnerArmor("jungle_skeleton");
    public static final ModelLayerLocation JUNGLE_SKELETON_OUTER_ARMOR = registerOuterArmor("jungle_skeleton");


    private static ModelLayerLocation registerInnerArmor(String name) {
        return register(name + "_inner_armor");
    }

    private static ModelLayerLocation registerOuterArmor(String name) {
        return register(name + "_outer_armor");
    }

    private static ModelLayerLocation register(String name) {
        return ToadClientUtils.addMain(HPlus.MODID, name);
    }

}
