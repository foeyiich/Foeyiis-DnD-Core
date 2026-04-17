package me.foeyii.fdndcore.manager.damage;

import me.foeyii.fdndcore.manager.dice.Dice;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DamagePool {
    private final Map<DamageType, List<Dice>> diceQueue = new LinkedHashMap<>();
    private final Map<DamageType, Integer> rolledResults = new LinkedHashMap<>();
    private boolean isRolled = false;

    public void addDice(DamageType type, Dice dice) {
        if (dice == null || dice.getCount() <= 0) return;

        diceQueue.computeIfAbsent(type, k -> new ArrayList<>()).add(dice);
    }

    public void rollAll(RandomSource random) {
        if (isRolled) return;

        diceQueue.forEach((type, listDadu) -> {
            int totalPerTipe = 0;

            for (Dice d : listDadu) {
                totalPerTipe += d.roll(random);
            }

            rolledResults.put(type, totalPerTipe);
        });

        isRolled = true;
    }

    public float getTotalDamage() {
        return (float) rolledResults.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Map<DamageType, Integer> getResults() {
        return rolledResults;
    }
}