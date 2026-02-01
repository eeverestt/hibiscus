package com.everest.hibiscus.api.modules.rendering.text;

import com.everest.hibiscus.api.modules.rendering.text.effects.WaveEffect;
import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffectManager;

public class HibiscusPresetEffects {
    public static final String WAVE_EFFECT = "wave";

    public static void init() {
        TextEffectManager.registerEffect(WAVE_EFFECT, new WaveEffect());
    }
}
