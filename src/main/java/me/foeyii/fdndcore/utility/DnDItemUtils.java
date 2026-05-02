package me.foeyii.fdndcore.utility;

import me.foeyii.fdndcore.data.DnDDataComponents;
import me.foeyii.fdndcore.system.damage.DamageType;
import me.foeyii.fdndcore.system.dice.Dice;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class DnDItemUtils {

    private static final Logger LOGGER = DnDLogger.getLogger(DnDItemUtils.class);

    private DnDItemUtils() {
        /* This utility class should not be instantiated */
    }

    public static void setOverrideDiceDamage(@NotNull ItemStack itemStack, @NotNull Dice dice) {
        itemStack.set(DnDDataComponents.Item.DICE_DAMAGE_OVERRIDE, dice);
    }

    public static void setOverrideDamageType(@NotNull ItemStack itemStack, @NotNull Holder<DamageType> damageType) {
        itemStack.set(DnDDataComponents.Item.DAMAGE_TYPE_OVERRIDE, damageType);
    }

    public static void setOverrideAttackDamageRoll(@NotNull ItemStack itemStack, int attackDamageRoll) {
        itemStack.set(DnDDataComponents.Item.ATTACK_ROLL_OVERRIDE, attackDamageRoll);
    }

}
