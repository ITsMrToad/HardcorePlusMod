package com.mr_toad.h_plus.client.renderer;

import com.mr_toad.h_plus.client.model.geom.HPModelLayers;
import com.mr_toad.h_plus.client.renderer.layer.ZombieBodyLayer;
import com.mr_toad.h_plus.common.entity.monster.FrostedZombie;
import com.mr_toad.h_plus.common.util.HPMiscUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class FrostedZombieRenderer extends AbstractZombieRenderer<FrostedZombie, ZombieModel<FrostedZombie>> {

    private static final ResourceLocation VAR_A = HPMiscUtils.makeRs("textures/entity/zombie/frosted_var_a.png");
    private static final ResourceLocation VAR_B = HPMiscUtils.makeRs("textures/entity/zombie/frosted_var_b.png");

    private static final ResourceLocation ICE = HPMiscUtils.makeRs("textures/entity/zombie/layer/ice_layer.png");

    public FrostedZombieRenderer(EntityRendererProvider.Context ctx) {
        this(ctx, HPModelLayers.FROSTED_ZOMBIE, HPModelLayers.FROSTED_ZOMBIE_INNER_ARMOR, HPModelLayers.FROSTED_ZOMBIE_OUTER_ARMOR);
        this.addLayer(new ZombieBodyLayer<>(this, ctx.getModelSet(), ICE, HPModelLayers.ZOMBIE_ICE));
    }

    public FrostedZombieRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation modelLayerLocation00, ModelLayerLocation modelLayerLocation01, ModelLayerLocation modelLayerLocation02) {
        super(ctx, new ZombieModel<>(ctx.bakeLayer(modelLayerLocation00)), new ZombieModel<>(ctx.bakeLayer(modelLayerLocation01)), new ZombieModel<>(ctx.bakeLayer(modelLayerLocation02)));
    }

    @Override
    public ResourceLocation getTextureLocation(FrostedZombie zombie) {
        return this.getVariantTexture(zombie);
    }

    public ResourceLocation getVariantTexture(FrostedZombie frostedZombie) {
        return switch (frostedZombie.getVariant()) {
            case VAR_A -> VAR_A;
            case VAR_B -> VAR_B;
        };
    }
}
