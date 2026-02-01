package com.everest.hibiscus.mixin.api.text;

import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffect;
import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffectManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {

    @Shadow
    protected abstract int drawInternal(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType layerType, int backgroundColor, int light);

    @Inject(
            method = "draw(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;" +
                    "Lnet/minecraft/client/render/VertexConsumerProvider;" +
                    "Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hibiscus$drawWithEffect(OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light, CallbackInfoReturnable<Integer> cir) {
        TextEffect effect = extractEffectFromOrderedText(text);

        if (effect == null) {
            return;
        }

        try {
            int width = effect.render((TextRenderer)(Object)this, text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
            cir.setReturnValue(width);
        } catch (Exception e) {
            cir.setReturnValue(this.drawInternal(text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light));
        }
    }

    @Unique
    private TextEffect extractEffectFromOrderedText(OrderedText text) {
        final TextEffect[] foundEffect = {null};
        text.accept((index, style, codePoint) -> {
            TextEffect effect = TextEffectManager.getEffectFromStyle(style);
            if (effect != null) {
                foundEffect[0] = effect;
                return false;
            }
            return true;
        });
        return foundEffect[0];
    }
}
