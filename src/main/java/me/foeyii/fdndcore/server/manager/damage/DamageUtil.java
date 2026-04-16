package me.foeyii.fdndcore.server.manager.damage;

import me.foeyii.fdndcore.server.manager.dice.Dice;
import net.minecraft.util.RandomSource;

public class DamageUtil {

    public static int calculateDamage(int strength, Dice dice, RandomSource randomSource) {
        return strength + dice.roll(randomSource);
    }

    public static Dice baseDamageToDice(float baseDamage) {
        if (baseDamage == 0) return Dice.of(0, 0);
        if (baseDamage == 1) return Dice.of(1, 1);
        int rollCount = Math.clamp((int) (baseDamage / Dice.MAX_DICE_SIDES), 1, Dice.MAX_DICE_COUNTS);
        int sides = Math.clamp((int) (baseDamage / rollCount), 1, Dice.MAX_DICE_SIDES);
        return Dice.of(rollCount, sides);
    }

}
