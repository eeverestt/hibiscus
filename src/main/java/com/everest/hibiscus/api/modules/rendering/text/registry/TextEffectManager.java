package com.everest.hibiscus.api.modules.rendering.text.registry;

import net.minecraft.text.Style;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TextEffectManager {
    private static final Map<String, TextEffect> EFFECT_REGISTRY = new ConcurrentHashMap<>();
    private static final String EFFECT_KEY = "hibiscus_effect";

    /**
     * Register an effect with a specific ID
     */
    public static void registerEffect(String effectId, TextEffect effect) {
        EFFECT_REGISTRY.put(effectId, effect);
    }

    /**
     * Register an effect and return the generated effect ID
     */
    public static String registerEffect(TextEffect effect) {
        String effectId = "effect_" + System.currentTimeMillis() + "_" + EFFECT_REGISTRY.size();
        registerEffect(effectId, effect);
        return effectId;
    }

    /**
     * Check if an effect ID is registered
     */
    public static boolean isEffectRegistered(String effectId) {
        return EFFECT_REGISTRY.containsKey(effectId);
    }

    /**
     * Unregister an effect by ID
     */
    public static void unregisterEffect(String effectId) {
        EFFECT_REGISTRY.remove(effectId);
    }

    /**
     * Get an effect by ID
     */
    public static TextEffect getEffect(String effectId) {
        return EFFECT_REGISTRY.get(effectId);
    }

    /**
     * Apply an effect to a style and return the modified style
     */
    public static Style withEffect(Style style, String effectId, TextEffect effect) {
        registerEffect(effectId, effect);
        return style.withInsertion(EFFECT_KEY + ":" + effectId);
    }

    /**
     * Apply an effect to a style with auto-generated ID
     */
    public static Style withEffect(Style style, TextEffect effect) {
        String effectId = registerEffect(effect);
        return style.withInsertion(EFFECT_KEY + ":" + effectId);
    }

    /**
     * Extract effect ID from style
     */
    public static String getEffectIdFromStyle(Style style) {
        if (style.getInsertion() != null && style.getInsertion().startsWith(EFFECT_KEY + ":")) {
            return style.getInsertion().substring((EFFECT_KEY + ":").length());
        }
        return null;
    }

    /**
     * Get effect from style
     */
    public static TextEffect getEffectFromStyle(Style style) {
        String effectId = getEffectIdFromStyle(style);
        return effectId != null ? EFFECT_REGISTRY.get(effectId) : null;
    }

    /**
     * Clear all registered effects (useful for development/reloading)
     */
    public static void clearAllEffects() {
        EFFECT_REGISTRY.clear();
    }

    /**
     * Get all registered effect IDs
     */
    public static Iterable<String> getRegisteredEffectIds() {
        return EFFECT_REGISTRY.keySet();
    }

    /**
     * Check if a style has an effect
     */
    public static boolean hasEffect(Style style) {
        return getEffectIdFromStyle(style) != null;
    }

    /**
     * Remove effect from style
     */
    public static Style removeEffect(Style style) {
        if (hasEffect(style)) {
            return style.withInsertion(null);
        }
        return style;
    }
}