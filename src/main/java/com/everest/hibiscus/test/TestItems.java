package com.everest.hibiscus.test;

import com.everest.hibiscus.Hibiscus;
import com.everest.hibiscus.test.item.TestItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class TestItems {
    public static final Item TEST_ITEM = register(
            new TestItem(new Item.Settings().rarity(Rarity.RARE)),
            "test_item"
    );

    public static Item register(Item item, String id) {
        Identifier itemID = Identifier.of(Hibiscus.MODID, id);
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
        return registeredItem;
    }

    public static void initialize() {}
}
