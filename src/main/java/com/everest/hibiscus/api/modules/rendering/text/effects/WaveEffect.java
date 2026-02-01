package com.everest.hibiscus.api.modules.rendering.text.effects;

import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffect;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;

public class WaveEffect implements TextEffect {
    private final float speed;
    private final float amplitude;
    private final float spacing;

    public WaveEffect() {
        this(0.15f, 2.0f, 0.6f);
    }

    public WaveEffect(float speed, float amplitude, float spacing) {
        this.speed = speed;
        this.amplitude = amplitude;
        this.spacing = spacing;
    }

    @Override
    public int render(TextRenderer textRenderer, OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light) {
        float time = (System.nanoTime() / 1_000_000L) / 50f;
        final float[] cursor = {0f};

        text.accept((index, style, codePoint) -> {
            float wobble = (float) Math.sin(time * speed + index * spacing) * amplitude;
            String charStr = String.valueOf(Character.toChars(codePoint));
            int charColor = style.getColor() != null ? style.getColor().getRgb() : color;

            textRenderer.draw(
                    charStr,
                    x + cursor[0],
                    y + wobble,
                    charColor,
                    shadow,
                    matrix,
                    vertexConsumers,
                    layerType,
                    backgroundColor,
                    light
            );

            cursor[0] += textRenderer.getWidth(charStr);
            return true;
        });

        return (int) cursor[0];
    }
}
