package com.mr_toad.h_plus.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
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

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class DesertSkeletonRenderer extends SkeletonRenderer {

    private static final ResourceLocation DESERT = HPMiscUtils.makeRs("textures/entity/skeleton/desert.png");
    private static final ResourceLocation BADLANDS = HPMiscUtils.makeRs("textures/entity/skeleton/badlands.png");

    public DesertSkeletonRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, HPModelLayers.DESERT_SKELETON, HPModelLayers.DESERT_SKELETON_INNER_ARMOR, HPModelLayers.DESERT_SKELETON_OUTER_ARMOR);
    }

    @Override
    protected void scale(AbstractSkeleton skeleton, PoseStack stack, float f00) {
        stack.scale(1.2F, 1.2F, 1.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton skeleton) {
        return DESERT;
    }
}
