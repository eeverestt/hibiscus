package com.everest.hibiscus;

import com.everest.hibiscus.api.modules.rendering.text.HibiscusPresetEffects;
import com.everest.hibiscus.test.TestItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Hibiscus implements ModInitializer {
    public static final String MODID = "hibiscus";

    @Override
    public void onInitialize() {
        TestItems.initialize();
        HibiscusPresetEffects.init();
    }

    public static Identifier id(String s) {
        return Identifier.of(MODID, s);
    }
}
