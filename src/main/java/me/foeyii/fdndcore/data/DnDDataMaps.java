package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.map.CombatItem;
import me.foeyii.fdndcore.data.map.EnchantmentBonus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = DnDCore.MODID)
public class DnDDataMaps {
    private DnDDataMaps() {
        /* This utility class should not be instantiated */
    }

    public static final DataMapType<Item, CombatItem> COMBAT_ITEMS = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "combat_items"),
            Registries.ITEM,
            CombatItem.CODEC
    ).build();

    public static final DataMapType<Enchantment, EnchantmentBonus> ENCHANTMENT_BONUSES = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "enchantment_bonuses"),
            Registries.ENCHANTMENT,
            EnchantmentBonus.CODEC
    ).build();

    @SubscribeEvent
    static void register(RegisterDataMapTypesEvent event) {
        event.register(COMBAT_ITEMS);
        event.register(ENCHANTMENT_BONUSES);
    }

}
