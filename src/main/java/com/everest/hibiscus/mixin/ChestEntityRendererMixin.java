package com.everest.hibiscus.mixin;

import com.everest.hibiscus.api.modules.rendering.util.WorldRenderOptions;
import com.everest.hibiscus.api.modules.rendering.world.SkyBeamRenderer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public class ChestEntityRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("TAIL")
    )
    private void test(BlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (entity instanceof ChestBlockEntity chest) {
            WorldRenderOptions o = new WorldRenderOptions()
                    .size(3f)
                    .animation(WorldRenderOptions.AnimationStyle.WOBBLE, 0.8f);
            SkyBeamRenderer.render(matrices, vertexConsumers, Identifier.of("moew", "nmew"), 270, 300, o);
        }
    }
}

