package me.foeyii.fdndcore.data.provider.subprovider;

import me.foeyii.fdndcore.data.DnDDamageTypes;
import me.foeyii.fdndcore.data.DnDTags;
import me.foeyii.fdndcore.data.map.WeaponProperties;
import me.foeyii.fdndcore.system.dice.Dice;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

public final class DnDWeaponPropertiesSubProvider {
    private DnDWeaponPropertiesSubProvider() {
        /* This utility class should not be instantiated */
    }

    public static void register(DataMapProvider.@NotNull Builder<WeaponProperties, Item> builder) {
        builder

                .add(DnDTags.Item.BLUDGEONING_WEAPONS,
                        new WeaponProperties(
                                null,
                                DnDDamageTypes.Physical.BLUDGEONING,
                                0,
                                null),
                        false
                )
                .add(DnDTags.Item.PIERCING_WEAPONS,
                        new WeaponProperties(
                                null,
                                DnDDamageTypes.Physical.PIERCING,
                                0,
                                null),
                        false
                )
                .add(DnDTags.Item.SLASHING_WEAPONS,
                        new WeaponProperties(
                                null,
                                DnDDamageTypes.Physical.PIERCING,
                                0,
                                null),
                        false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.WOODEN_SWORD),
                        new WeaponProperties(
                                new Dice(1, 2, 1),
                                null,
                                1,
                                null
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.STONE_SWORD),
                        new WeaponProperties(
                                new Dice(1, 4, 0),
                                null,
                                1,
                                null
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.GOLDEN_SWORD),
                        new WeaponProperties(
                                new Dice(1, 2, 1),
                                null,
                                3,
                                null
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_SWORD),
                        new WeaponProperties(
                                new Dice(1, 6, 0),
                                null,
                                1,
                                null
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND_SWORD),
                        new WeaponProperties(
                                new Dice(1, 6, 1),
                                null,
                                1,
                                null
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.NETHERITE_SWORD),
                        new WeaponProperties(
                                new Dice(1, 8, 0),
                                null,
                                1,
                                null
                        ), false
                )
        ;
    }

}
