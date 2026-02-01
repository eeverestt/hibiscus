package com.everest.hibiscus.api.modules.rendering.text;

import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffect;
import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffectManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TextEffects {

    /**
     * Apply an effect to text with a specific effect ID
     */
    public static MutableText withEffect(Text text, String effectId, TextEffect effect) {
        TextEffectManager.registerEffect(effectId, effect);
        MutableText mutable = text.copy();
        mutable.setStyle(TextEffectManager.withEffect(mutable.getStyle(), effectId, effect));
        return mutable;
    }

    /**
     * Apply an effect to text with auto-generated ID
     */
    public static MutableText withEffect(Text text, TextEffect effect) {
        String effectId = TextEffectManager.registerEffect(effect);
        MutableText mutable = text.copy();
        mutable.setStyle(TextEffectManager.withEffect(mutable.getStyle(), effectId, effect));
        return mutable;
    }

    /**
     * Apply an effect to existing MutableText
     */
    public static void applyEffect(MutableText text, String effectId, TextEffect effect) {
        TextEffectManager.registerEffect(effectId, effect);
        text.setStyle(TextEffectManager.withEffect(text.getStyle(), effectId, effect));
    }

    /**
     * Apply an effect to existing MutableText with auto-generated ID
     */
    public static void applyEffect(MutableText text, TextEffect effect) {
        String effectId = TextEffectManager.registerEffect(effect);
        text.setStyle(TextEffectManager.withEffect(text.getStyle(), effectId, effect));
    }

    /**
     * Check if text has an effect
     */
    public static boolean hasEffect(Text text) {
        return TextEffectManager.hasEffect(text.getStyle());
    }

    /**
     * Get the effect from text
     */
    public static TextEffect getEffect(Text text) {
        return TextEffectManager.getEffectFromStyle(text.getStyle());
    }

    /**
     * Remove effect from text
     */
    public static void removeEffect(MutableText text) {
        text.setStyle(TextEffectManager.removeEffect(text.getStyle()));
    }
}