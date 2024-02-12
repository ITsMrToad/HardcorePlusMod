package com.mr_toad.h_plus.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mr_toad.h_plus.common.entity.monster.baby.BabyCaveSpider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class BabyCaveSpiderRenderer extends SpiderRenderer<BabyCaveSpider> {

    private static final ResourceLocation CAVE_SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/cave_spider.png");

    public BabyCaveSpiderRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.shadowRadius *= 0.4F;
    }

    @Override
    protected void scale(BabyCaveSpider spider, PoseStack stack, float f0) {
        stack.scale(0.4F, 0.4F, 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(BabyCaveSpider spider) {
        return CAVE_SPIDER_LOCATION;
    }
}
