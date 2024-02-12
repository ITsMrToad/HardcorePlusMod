package com.mr_toad.h_plus.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mr_toad.h_plus.common.entity.monster.baby.BabySpider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class BabySpiderRenderer extends SpiderRenderer<BabySpider> {

    public BabySpiderRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    protected void scale(BabySpider spider, PoseStack stack, float f0) {
        stack.scale(0.5F, 0.5F, 0.5F);
    }
}
