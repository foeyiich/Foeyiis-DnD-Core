package me.foeyii.fdndcore.manager.damage;

import me.foeyii.fdndcore.manager.dice.Dice;
import net.minecraft.util.RandomSource;

public class DamageUtil {

    public static int calculateDamage(int strength, Dice dice, RandomSource randomSource) {
        return strength + dice.roll(randomSource);
    }

    public static Dice baseDamageToDice(float baseDamage) {
        if (baseDamage <= 1) return Dice.of(1, 1);

        // Jika damage 4, langsung arahkan ke 1d4
        // Jika damage 7, bisa jadi 1d6 + 1 (modifier) atau 2d3 (custom)
        int sides = (int) baseDamage;

        // Batasi sides ke angka genap standar jika ingin terasa autentik
        if (sides > 1 && sides % 2 != 0) sides++;

        int rollCount = 1;
        if (sides > 12) {
            rollCount = sides / 6; // Contoh: damage 24 jadi 4d6
            sides = 6;
        }

        return Dice.of(rollCount, Math.clamp(sides, 1, Dice.MAX_DICE_SIDES));
    }


}
