package com.everest.hibiscus.test.item;

import com.everest.hibiscus.Hibiscus;
import com.everest.hibiscus.api.modules.gui.item.VariableTexture;
import com.everest.hibiscus.api.modules.rendering.text.HibiscusPresetEffects;
import com.everest.hibiscus.api.modules.rendering.text.TextEffects;
import com.everest.hibiscus.api.modules.rendering.text.registry.TextEffectManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TestItem extends Item implements VariableTexture {
    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return TextEffects.withEffect(
                Text.literal("Test Item"),
                HibiscusPresetEffects.WAVE_EFFECT,
                TextEffectManager.getEffect(HibiscusPresetEffects.WAVE_EFFECT)
        );
    }

    @Override
    public Identifier getTexture(ItemStack stack) {
        return Identifier.of(Hibiscus.MODID, "test/test_item_gui");
    }
}
