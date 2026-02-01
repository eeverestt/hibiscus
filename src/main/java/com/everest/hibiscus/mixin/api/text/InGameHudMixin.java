package com.everest.hibiscus.mixin.api.text;

import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffect;
import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow public ItemStack currentStack;
    @Shadow private int heldItemTooltipFade;
    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void hibiscus$renderHeldItemName(DrawContext context, CallbackInfo ci) {
        if (currentStack.isEmpty() || heldItemTooltipFade <= 0) return;

        Text itemName = currentStack.getName();
        TextEffect effect = TextEffectManager.getEffectFromStyle(itemName.getStyle());
        if (effect == null) return;

        int width = getTextRenderer().getWidth(itemName);
        int x = (context.getScaledWindowWidth() - width) / 2;
        int y = context.getScaledWindowHeight() - 59;
        if (!client.interactionManager.hasStatusBars()) y += 14;

        Integer styleColor = itemName.getStyle().getColor() != null ? itemName.getStyle().getColor().getRgb() : null;

        Integer rarityColor = currentStack.getRarity().getFormatting() != null
                ? currentStack.getRarity().getFormatting().getColorValue()
                : null;

        int baseColor = styleColor != null ? styleColor : (rarityColor != null ? rarityColor : 0xFFFFFF);

        float alpha = Math.min(1.0f, heldItemTooltipFade / 10.0f);
        int argb = ((int)(alpha * 255) << 24) | (baseColor & 0xFFFFFF);

        effect.render(getTextRenderer(), itemName.asOrderedText(), x, y, argb, true, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.POLYGON_OFFSET, 0, 15728880);

        ci.cancel();
    }
}
