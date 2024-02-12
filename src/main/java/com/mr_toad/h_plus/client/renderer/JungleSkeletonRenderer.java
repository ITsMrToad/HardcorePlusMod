package com.mr_toad.h_plus.client.renderer;

import com.mr_toad.h_plus.client.model.geom.HPModelLayers;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class JungleSkeletonRenderer extends SkeletonRenderer {

    private static final ResourceLocation SKELETON_LOCATION = HPMiscUtils.makeRs("textures/entity/skeleton/jungle.png");

    public JungleSkeletonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, HPModelLayers.JUNGLE_SKELETON, HPModelLayers.JUNGLE_SKELETON_INNER_ARMOR, HPModelLayers.JUNGLE_SKELETON_OUTER_ARMOR);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton skeleton) {
        return SKELETON_LOCATION;
    }
}
