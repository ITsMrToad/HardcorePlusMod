package com.mr_toad.h_plus.client.renderer;

import com.mr_toad.h_plus.client.model.geom.HPModelLayers;
import com.mr_toad.h_plus.client.renderer.layer.ZombieBodyLayer;
import com.mr_toad.h_plus.common.entity.monster.JungleZombie;
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
public class JungleZombieRenderer extends AbstractZombieRenderer<JungleZombie, ZombieModel<JungleZombie>> {

    private static final ResourceLocation VAR_A = HPMiscUtils.makeRs("textures/entity/zombie/jungle_var_a.png");
    private static final ResourceLocation VAR_B = HPMiscUtils.makeRs("textures/entity/zombie/jungle_var_b.png");

    private static final ResourceLocation MOSS = HPMiscUtils.makeRs("textures/entity/zombie/layer/moss_layer.png");

    public JungleZombieRenderer(EntityRendererProvider.Context ctx) {
        this(ctx, HPModelLayers.JUNGLE_ZOMBIE, HPModelLayers.JUNGLE_ZOMBIE_INNER_ARMOR, HPModelLayers.JUNGLE_ZOMBIE_OUTER_ARMOR);
        this.addLayer(new ZombieBodyLayer<>(this, ctx.getModelSet(), MOSS, HPModelLayers.ZOMBIE_MOSS));
    }

    public JungleZombieRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation modelLayerLocation00, ModelLayerLocation modelLayerLocation01, ModelLayerLocation modelLayerLocation02) {
        super(ctx, new ZombieModel<>(ctx.bakeLayer(modelLayerLocation00)), new ZombieModel<>(ctx.bakeLayer(modelLayerLocation01)), new ZombieModel<>(ctx.bakeLayer(modelLayerLocation02)));
    }

    @Override
    public ResourceLocation getTextureLocation(JungleZombie zombie) {
        return this.getVariantTexture(zombie);
    }

    public ResourceLocation getVariantTexture(JungleZombie jungleZombie) {
        return switch (jungleZombie.getVariant()) {
            case VAR_A -> VAR_A;
            case VAR_B -> VAR_B;
        };
    }


}
