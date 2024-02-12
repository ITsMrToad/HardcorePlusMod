package com.mr_toad.h_plus.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ZombieBodyLayer<T extends Zombie, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final ZombieModel<T> layerModel;
    private final ResourceLocation texRl;

    public ZombieBodyLayer(RenderLayerParent<T, M> parent, EntityModelSet set, ResourceLocation texRl, ModelLayerLocation modelLayerLocation) {
        super(parent);
        this.layerModel = new ZombieModel<>(set.bakeLayer(modelLayerLocation));
        this.texRl = texRl;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource source, int i0, T e, float f, float f0, float f1, float f2, float f3, float f4) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, texRl, stack, source, i0, e, f, f0, f2, f3, f4, f1, 1.0F, 1.0F, 1.0F);
    }
}