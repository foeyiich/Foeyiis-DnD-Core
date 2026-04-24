package me.foeyii.fdndcore.utility;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.DnDAttributes;
import me.foeyii.fdndcore.data.DnDDataComponents;
import me.foeyii.fdndcore.data.map.CombatItem;
import me.foeyii.fdndcore.data.map.EnchantmentBonus;
import me.foeyii.fdndcore.dice.Dice;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DnDItemUtils {
    private DnDItemUtils() {
        /* This utility class should not be instantiated */
    }

    public static void setCustomDiceDamage(@NotNull ItemStack stack, @NotNull Dice dice) {
        stack.set(DnDDataComponents.DICE_DAMAGE, dice);
    }

    public static @Nullable Dice getCustomDiceDamage(@NotNull ItemStack stack) {
        return stack.get(DnDDataComponents.DICE_DAMAGE);
    }

    public static @Nullable Dice getBaseDiceDamage(@NotNull Item item) {

        CombatItem combatItem = CombatItem.get(item);
        if (combatItem == null) {
            DnDCore.LOGGER.info("No Combat Item Provided. ({})", item);
            return null;
        }
        return combatItem.dice().orElseGet(() -> {
            DnDCore.LOGGER.info("No Dice Provided for {}", item);
            return null;
        });

    }

    public static @NotNull Dice getDice(@NotNull ItemStack stack, @NotNull Level level) {

        // Trying to get item has custom dice damage
        Dice dice = getCustomDiceDamage(stack);
        if (dice != null) return dice;

        // Trying to get item has base dice damage set from DnDDataMaps
        dice = getBaseDiceDamage(stack.getItem());
        if (dice != null) return dice;

        // Calculate dice
        dice = calculateDiceFrom(stack);

        // Calculate Enchantment Modifier
        Set<EnchantmentBonus> enchantmentsModifier = getEnchantmentBonus(stack);
        if (enchantmentsModifier.isEmpty()) return dice;

        int newDiceCount = dice.count();
        int newDiceSides = dice.sides();
        int newDiceModifier = dice.modifier();
        for (EnchantmentBonus enchantmentBonus : enchantmentsModifier) {
            newDiceCount += enchantmentBonus.count();
            newDiceSides += enchantmentBonus.sides();
            newDiceModifier += enchantmentBonus.modifier();
        }

        return Dice.of(newDiceCount, newDiceSides, newDiceModifier);

    }

    public static Dice calculateDiceFrom(@NotNull ItemStack stack) {
        var baseAttrDamage = stack.getAttributeModifiers().modifiers()
                .stream()
                .filter(entry -> entry.attribute() == Attributes.ATTACK_DAMAGE)
                .findFirst();
        double baseDamage = 0;
        if (baseAttrDamage.isPresent()) {
            baseDamage = baseAttrDamage.get().modifier().amount();
        }
        return Dice.from((int) baseDamage);
    }

    public static Set<EnchantmentBonus> getEnchantmentBonus(@NotNull ItemStack stack) {
        Set<EnchantmentBonus> enchantmentBonuses = new HashSet<>();
        var enchantments = stack.getTagEnchantments().entrySet();
        for (var entry : enchantments) {
            EnchantmentBonus enchantmentBonus = EnchantmentBonus.get(entry.getKey());
            if (enchantmentBonus == null) break;
            enchantmentBonuses.add(enchantmentBonus);
        }
        return enchantmentBonuses;
    }

    public static int getAttackRollBonus(@NotNull ItemStack stack) {
        return stack.getAttributeModifiers().modifiers()
                .stream()
                .filter(entry -> entry.attribute() == DnDAttributes.ATTACK_ROLL_BONUS)
                .findFirst()
                .map(entry -> (int) entry.modifier().amount()).orElse(0);
    }

}
