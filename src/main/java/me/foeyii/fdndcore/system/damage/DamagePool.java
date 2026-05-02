package me.foeyii.fdndcore.system.damage;

import me.foeyii.fdndcore.system.dice.DamageDice;
import me.foeyii.fdndcore.system.dice.Dice;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DamagePool {
    private final Map<DamageType, List<Dice>> diceQueue = new LinkedHashMap<>();
    private final Map<DamageType, Integer> rolledResults = new LinkedHashMap<>();
    private boolean isRolled = false;

    public void addDice(@NotNull DamageDice damageDice) {
        Dice dice = damageDice.baseDice();
        if (dice.count() == 0 && dice.modifier() == 0) return;

        diceQueue.computeIfAbsent(damageDice.damageType().value(), k -> new ArrayList<>()).add(dice);
    }

    public void rollAll(RandomSource random) {
        if (isRolled) return;

        diceQueue.forEach((type, listDadu) -> {
            int typeTotalDamage = 0;

            for (Dice d : listDadu) {
                typeTotalDamage += d.roll(random);
            }

            rolledResults.put(type, typeTotalDamage);
        });

        isRolled = true;
    }

    public float getTotalDamage() {
        return rolledResults.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Map<DamageType, Integer> getResults() {
        return rolledResults;
    }
}