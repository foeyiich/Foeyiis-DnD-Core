package me.foeyii.fdndcore.utility;

import me.foeyii.fdndcore.data.DnDDamageTypes;
import me.foeyii.fdndcore.data.DnDDataComponents;
import me.foeyii.fdndcore.data.DnDTags;
import me.foeyii.fdndcore.data.map.WeaponProperties;
import me.foeyii.fdndcore.enchantment.DiceEnchantmentBonus;
import me.foeyii.fdndcore.enchantment.DiceEnchantmentEffect;
import me.foeyii.fdndcore.system.damage.DamageType;
import me.foeyii.fdndcore.system.dice.DamageDice;
import me.foeyii.fdndcore.system.dice.Dice;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DnDDamageResolver {
    private DnDDamageResolver() {
        /* This utility class should not be instantiated */
    }

    public static @NotNull Holder<DamageType> findDefaultDamageType(@NotNull ItemStack itemStack) {
        if (itemStack.is(DnDTags.Item.BLUDGEONING_WEAPONS)) return DnDDamageTypes.Physical.BLUDGEONING;
        if (itemStack.is(DnDTags.Item.PIERCING_WEAPONS)) return DnDDamageTypes.Physical.PIERCING;
        if (itemStack.is(DnDTags.Item.SLASHING_WEAPONS)) return DnDDamageTypes.Physical.SLASHING;
        return DnDDamageTypes.UNKNOWN;
    }

    public static @NotNull Holder<DamageType> findOverrideDamageType(@NotNull ItemStack itemStack) {
        Holder<DamageType> overrideType = itemStack.get(DnDDataComponents.Item.DAMAGE_TYPE_OVERRIDE);
        return overrideType != null ? overrideType : DnDDamageTypes.UNKNOWN;
    }

    public static @NotNull Holder<DamageType> findPredefinedDamageType(@NotNull ItemStack itemStack) {
        WeaponProperties props = WeaponProperties.get(itemStack.getItemHolder());
        return props.overrideType().orElse(DnDDamageTypes.UNKNOWN);
    }

    public static @NotNull Holder<DamageType> resolveEffectiveDamageType(@NotNull ItemStack itemStack) {
        Holder<DamageType> damageType = findOverrideDamageType(itemStack);
        if (damageType != DnDDamageTypes.UNKNOWN) return damageType;

        damageType = findPredefinedDamageType(itemStack);
        if (damageType != DnDDamageTypes.UNKNOWN) return damageType;

        return findDefaultDamageType(itemStack);
    }

    public static @NotNull Dice findOverrideDiceDamage(@NotNull ItemStack itemStack) {
        Dice overrideDice = itemStack.get(DnDDataComponents.Item.DICE_DAMAGE_OVERRIDE);
        if (overrideDice == null) return Dice.EMPTY;
        return overrideDice;
    }

    public static @NotNull Dice findPredefinedDiceDamage(@NotNull ItemStack itemStack) {
        WeaponProperties props = WeaponProperties.get(itemStack.getItemHolder());
        return props.dice().orElse(Dice.EMPTY);
    }

    public static @NotNull Dice resolveDiceDamageByAttribute(@NotNull ItemStack itemStack) {
        var baseAttrDamage = itemStack.getAttributeModifiers().modifiers()
                .stream()
                .filter(entry -> entry.attribute() == Attributes.ATTACK_DAMAGE)
                .findFirst();
        double baseDamage = 0;
        if (baseAttrDamage.isPresent()) {
            baseDamage = baseAttrDamage.get().modifier().amount();
        }
        return Dice.from((int) baseDamage);
    }

    public static @NotNull List<DamageDice> resolveDiceDamageByEnchantment(@NotNull ItemStack itemStack, @NotNull LivingEntity target) {
        List<DamageDice> diceAdditions = new ArrayList<>();

        for (var entry : itemStack.getTagEnchantments().entrySet()) {
            DiceEnchantmentBonus bonus = DiceEnchantmentBonus.get(entry.getKey());
            if (bonus == null) continue;

            int level = entry.getIntValue();

            for (DiceEnchantmentEffect effect : bonus.effects()) {
                if (!effect.appliesTo(target)) continue;

                Dice dice = effect.getDice(level);
                if (dice.isEmpty()) continue;

                Holder<DamageType> type = effect.damageType().orElse(resolveEffectiveDamageType(itemStack));
                diceAdditions.add(DamageDice.of(dice, type));
            }
        }
        return diceAdditions;
    }

    public static @NotNull Dice resolveEffectiveDiceDamage(@NotNull ItemStack itemStack) {
        Dice dice = findOverrideDiceDamage(itemStack);
        if (dice != Dice.EMPTY) return dice;

        dice = findPredefinedDiceDamage(itemStack);
        if (dice != Dice.EMPTY) return dice;

        return resolveDiceDamageByAttribute(itemStack);
    }

    public static Integer findPredefinedAttackRollBonus(@NotNull ItemStack itemStack) {
        WeaponProperties props = WeaponProperties.get(itemStack.getItemHolder());
        return props.attackRollBonus().orElse(null);
    }

    public static Integer findOverrideAttackRollBonus(@NotNull ItemStack itemStack) {
        return itemStack.get(DnDDataComponents.Item.ATTACK_ROLL_OVERRIDE);
    }

    public static int resolveAttackRollBonus(@NotNull ItemStack itemStack) {
        Integer attackRollBonus = findOverrideAttackRollBonus(itemStack);
        if (attackRollBonus == null) attackRollBonus = findPredefinedAttackRollBonus(itemStack);
        return attackRollBonus;
    }
}

