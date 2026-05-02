package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.map.WeaponProperties;
import me.foeyii.fdndcore.enchantment.DiceEnchantmentBonus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = DnDCore.MODID)
public final class DnDDataMaps {
    private DnDDataMaps() {
        /* This utility class should not be instantiated */
    }

    public static final DataMapType<Item, WeaponProperties> WEAPON_PROPERTIES = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "weapon_properties"),
            Registries.ITEM,
            WeaponProperties.CODEC
    ).build();

    public static final DataMapType<Enchantment, DiceEnchantmentBonus> DICE_ENCHANTMENT_BONUSES = DataMapType.builder(
            ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "dice_enchantment_bonuses"),
            Registries.ENCHANTMENT,
            DiceEnchantmentBonus.CODEC
    ).build();

    @SubscribeEvent
    static void register(RegisterDataMapTypesEvent event) {
        event.register(WEAPON_PROPERTIES);
        event.register(DICE_ENCHANTMENT_BONUSES);
    }

}
