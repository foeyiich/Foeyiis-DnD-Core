package me.foeyii.fdndcore.manager.damage;

import me.foeyii.fdndcore.manager.dice.Dice;
import me.foeyii.fdndcore.manager.dice.DiceNotation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DamageItemManager {
    private DamageItemManager() {
        /* This utility class should not be instantiated */
    }

    public static void setDice(@NotNull ItemStack stack, @NotNull Dice dice) {
        stack.set(DamageItemDataComponent.DICE_DAMAGE, dice.toString());
    }

    public static @NotNull Dice getBaseDice(@NotNull Item item) {

        ItemAttributeModifiers attributeModifiers = item.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
        double damageValue = 0;

        if (attributeModifiers != null) {
            for (var entry : attributeModifiers.modifiers()) {
                if (entry.attribute().is(Attributes.ATTACK_DAMAGE)) {
                    damageValue += entry.modifier().amount();
                }
            }
        }

        return Dice.generateFromInt((int) damageValue);
    }

    public static Dice getDice(@NotNull ItemStack stack, @NotNull Level level) {


        Dice baseDice = getBaseDice(stack.getItem());
        if (baseDice.getCount() <= 0) return Dice.of(DiceNotation.stringify(0, 0));

        var enchantmentRegistries = level.registryAccess().holderOrThrow(Enchantments.SHARPNESS);
        var sharpnessLevel = stack.getEnchantmentLevel(enchantmentRegistries);

        return Dice.of(baseDice.getCount(), baseDice.getSides() + sharpnessLevel);

    }

}
