package me.foeyii.fdndcore.server.manager.damage;

import me.foeyii.fdndcore.server.manager.dice.Dice;
import net.minecraft.util.RandomSource;

import java.util.LinkedHashMap;
import java.util.Map;

public class DamagePool {
    private final Map<DamageType, Dice> diceQueue = new LinkedHashMap<>();
    private final Map<DamageType, Integer> rolledResults = new LinkedHashMap<>();
    private boolean isRolled = false;

    public void addDice(DamageType type, Dice dice) {
        if (dice == null || dice.getCount() <= 0) return;

        diceQueue.merge(type, dice, Dice::plus);
    }

    public void rollAll(RandomSource random) {
        if (isRolled) return;

        diceQueue.forEach((type, dice) -> {
            rolledResults.put(type, dice.roll(random));
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