package com.everest.hibiscus.api.modules.rendering.text.registry;

import net.minecraft.text.MutableText;

public class EffectText {
    private final MutableText text;
    private final TextEffect effect;
    private final String effectId;

    public EffectText(MutableText text, TextEffect effect) {
        this.text = text;
        this.effect = effect;
        this.effectId = "effect_" + System.identityHashCode(text);
    }

    public MutableText asText() {
        return text.setStyle(text.getStyle().withInsertion("hibiscus_effect:" + effectId));
    }

    public TextEffect getEffect() {
        return effect;
    }

    public String getEffectId() {
        return effectId;
    }
}