package com.everest.hibiscus.api.modules.rendering.text.registry;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import org.joml.Matrix4f;

public interface TextEffect {
    void renderTooltipText(TextRenderer textRenderer, OrderedText text, int x, int y, int color, float tickDelta);
}
