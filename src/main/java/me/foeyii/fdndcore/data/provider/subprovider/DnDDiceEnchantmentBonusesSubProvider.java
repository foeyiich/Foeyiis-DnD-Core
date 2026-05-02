package me.foeyii.fdndcore.data.provider.subprovider;

import me.foeyii.fdndcore.enchantment.DiceEnchantmentBonus;
import me.foeyii.fdndcore.enchantment.DiceEnchantmentEffect;
import me.foeyii.fdndcore.enchantment.DiceLevelScaling;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

public final class DnDDiceEnchantmentBonusesSubProvider {
    private DnDDiceEnchantmentBonusesSubProvider() {
    }

    public static void register(DataMapProvider.@NotNull Builder<DiceEnchantmentBonus, Enchantment> builder) {
        builder
                .add(Enchantments.SHARPNESS, new DiceEnchantmentBonus(
                        new DiceEnchantmentEffect(new DiceLevelScaling(
                                LevelBasedValue.constant(0),
                                LevelBasedValue.constant(0),
                                LevelBasedValue.perLevel(1, 1)
                        ))
                ), false)
        ;
    }
}
