package de.aelpecyem.runes.mixin.client;

import de.aelpecyem.runes.client.model.feature.StasisFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow protected abstract boolean addFeature(FeatureRenderer<T, M> feature);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererFactory.Context ctx, M model, float shadowRadius, CallbackInfo ci){
        this.addFeature((FeatureRenderer<T, M>) new StasisFeatureRenderer((FeatureRendererContext) this));
    }
}
