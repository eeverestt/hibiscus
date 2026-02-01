package com.everest.hibiscus.api.modules.rendering.text.registry;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;

public interface TextEffect {
    int render(TextRenderer textRenderer, OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light);

    default int getColorForCharacter(int index, int originalColor, int styleColor) {
        return styleColor != -1 ? styleColor : originalColor;
    }
}
