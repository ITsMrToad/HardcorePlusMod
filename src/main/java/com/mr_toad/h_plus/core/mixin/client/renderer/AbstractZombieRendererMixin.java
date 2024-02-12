package com.mr_toad.h_plus.core.mixin.client.renderer;

import com.mr_toad.h_plus.common.util.entitydata.ZombieConversionDataContainer;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractZombieRenderer.class)
public abstract class AbstractZombieRendererMixin<T extends Zombie, M extends ZombieModel<T>> extends HumanoidMobRenderer<T, M> {

    public AbstractZombieRendererMixin(EntityRendererProvider.Context ctx, M m, float f0) {
        super(ctx, m, f0);
    }

    @Inject(method = "isShaking(Lnet/minecraft/world/entity/monster/Zombie;)Z", at = @At("TAIL"), cancellable = true)
    protected void shouldShaking(T entity, CallbackInfoReturnable<Boolean> cir) {
        ZombieConversionDataContainer dataContainer = (ZombieConversionDataContainer) entity;
        cir.setReturnValue(cir.getReturnValue() || dataContainer.isShaking());
    }
}
