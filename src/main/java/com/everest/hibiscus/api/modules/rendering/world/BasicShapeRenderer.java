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

public class BasicShapeRenderer {

    /** Render a cube with full RenderOptions support */
    public static void renderCube(MatrixStack matrices, VertexConsumerProvider provider, Identifier texture, WorldRenderOptions options) {
        matrices.push();

        MinecraftClient client = MinecraftClient.getInstance();
        float time = client.world.getTime() + client.getRenderTickCounter().getTickDelta(false);

        // Apply SPIN animation
        if (options.animation == WorldRenderOptions.AnimationStyle.SPIN) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 4f * options.animationSpeed));
        }

        // Prepare vertex consumer
        VertexConsumer cons = provider.getBuffer(RenderLayer.getEntityTranslucent(texture));
        MatrixStack.Entry e = matrices.peek();

        float half = options.size * 0.5f;

        // Handle UV scroll & wobble
        float scroll = options.uvScroll * time;
        float wobble = 0f;
        if (options.animation == WorldRenderOptions.AnimationStyle.WOBBLE) {
            wobble = (float) Math.sin(time * 0.15f * options.animationSpeed) * 0.08f;
        }

        // Alpha handling
        float alpha = options.maxAlpha * 255f;

        if (options.fadeInTicks > 0) {
            alpha = MathHelper.clamp(time / options.fadeInTicks * 255f, 0f, alpha);
        }
        if (options.fadeOutTicks > 0) {
            alpha = MathHelper.clamp((options.fadeOutTicks - (time % options.fadeOutTicks)) / options.fadeOutTicks * 255f, 0f, alpha);
        }
        if (options.animation == WorldRenderOptions.AnimationStyle.PULSE) {
            alpha *= MathHelper.sin(time * 0.1f * options.animationSpeed) * 0.5f + 0.5f;
        }

        // Cube vertices (simplified as 6 quads)
        float[][] corners = {
                {-half, -half, -half}, {half, -half, -half}, {half, half, -half}, {-half, half, -half},
                {-half, -half, half}, {half, -half, half}, {half, half, half}, {-half, half, half}
        };

        int[][] faces = {
                {0, 1, 2, 3}, // front
                {5, 4, 7, 6}, // back
                {4, 0, 3, 7}, // left
                {1, 5, 6, 2}, // right
                {3, 2, 6, 7}, // top
                {4, 5, 1, 0}  // bottom
        };

        for (int[] face : faces) {
            for (int i = 0; i < 4; i++) {
                float[] v = corners[face[i]];
                float u = (v[0] + half) + wobble;
                float vCoord = (v[2] + half) + scroll;
                cons.vertex(e, v[0], v[1], v[2])
                        .color(255, 255, 255, (int) alpha)
                        .texture(u, vCoord)
                        .light(0xF000F0)
                        .normal(e, 0, 1, 0)
                        .overlay(OverlayTexture.DEFAULT_UV);
            }
        }

        matrices.pop();
    }
}
