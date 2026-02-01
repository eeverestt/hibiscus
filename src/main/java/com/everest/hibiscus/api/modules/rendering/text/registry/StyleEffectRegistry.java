package com.everest.hibiscus.api.modules.rendering.text.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StyleEffectRegistry {
    private static final Map<String, TextEffect> EFFECTS = new ConcurrentHashMap<>();

    public static void register(String effectId, TextEffect effect) {
        EFFECTS.put(effectId, effect);
    }

    public static TextEffect get(String effectId) {
        return EFFECTS.get(effectId);
    }

    public static void remove(String effectId) {
        EFFECTS.remove(effectId);
    }
}