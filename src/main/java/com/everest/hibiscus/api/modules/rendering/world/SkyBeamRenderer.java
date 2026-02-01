package com.everest.hibiscus.api.modules.rendering.world;

import com.everest.hibiscus.api.modules.rendering.util.WorldRenderOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class SkyBeamRenderer {

    public static void render(MatrixStack matrices, VertexConsumerProvider provider, Identifier texture, float tiltAngleDeg, float height, WorldRenderOptions options) {
        matrices.push();

        var cam = MinecraftClient.getInstance().gameRenderer.getCamera();
        float yaw = cam.getYaw();

        if (options.animation == WorldRenderOptions.AnimationStyle.SPIN) {
            float time = MinecraftClient.getInstance().world.getTime()
                    + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 4f * options.animationSpeed));
        } else {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
        }

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(tiltAngleDeg));

        VertexConsumer cons = provider.getBuffer(RenderLayer.getEntityTranslucent(texture));
        MatrixStack.Entry e = matrices.peek();

        float time = MinecraftClient.getInstance().world.getTime()
                + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);

        float wobbleU = 0f;
        if (options.animation == WorldRenderOptions.AnimationStyle.WOBBLE) {
            wobbleU = (float) Math.sin(time * 0.15f * options.animationSpeed) * 0.08f;
        }

        float halfWidth = options.size * 0.5f;

        float x1 = -halfWidth;
        float x2 = halfWidth;
        float zTop = height;

        float vRepeat = height * 0.10f;

        float alphaBottom = options.maxAlpha;
        float alphaTop = options.maxAlpha;

        if (options.fadeInTicks > 0)
            alphaBottom = MathHelper.clamp(time / options.fadeInTicks, 0f, alphaBottom);

        if (options.fadeOutTicks > 0)
            alphaTop = MathHelper.clamp(
                    (options.fadeOutTicks - (time % options.fadeOutTicks)) / options.fadeOutTicks,
                    0f, alphaTop
            );

        if (options.animation == WorldRenderOptions.AnimationStyle.PULSE) {
            float pulse = MathHelper.sin(time * 0.1f * options.animationSpeed) * 0.5f + 0.5f;
            alphaBottom *= pulse;
            alphaTop *= pulse;
        }

        float scroll = (time * options.uvScroll);

        put(cons, e, x1, 0,     0,     wobbleU,     scroll,           alphaBottom);
        put(cons, e, x1, 0,     zTop,  wobbleU,     scroll + vRepeat, alphaTop);
        put(cons, e, x2, 0,     zTop,  wobbleU + 1, scroll + vRepeat, alphaTop);
        put(cons, e, x2, 0,     0,     wobbleU + 1, scroll,           alphaBottom);

        matrices.pop();
    }

    private static void put(VertexConsumer cons, MatrixStack.Entry e,
                            float x, float y, float z,
                            float u, float v,
                            float alpha) {

        cons.vertex(e.getPositionMatrix(), x, y, z)
                .color(1f, 1f, 1f, alpha)
                .texture(u, v)
                .light(0xF000F0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(e, 0, 1, 0);
    }
}
