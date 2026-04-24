package me.foeyii.fdndcore.logic.combat;

import me.foeyii.fdndcore.abilityscore.AbilityScore;
import me.foeyii.fdndcore.abilityscore.AbilityScoreContainer;
import me.foeyii.fdndcore.dice.Dice;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class CombatEngine {
    private CombatEngine() {
        /* This utility class should not be instantiated */
    }

    public static final int BASE_ARMOR_CLASS = 10;
    public static final int MAX_ARMOR_CLASS = Integer.MAX_VALUE;
    public static final int MAX_DAMAGE_REDUCTION = 10;

    public static int getArmorClass(@NotNull LivingEntity entity) {
        int dexModifier = 0;

        AttributeInstance armorAttr = entity.getAttribute(Attributes.ARMOR);
        int armorValue = (armorAttr != null) ? (int) armorAttr.getValue() : 0;

        return Math.clamp(BASE_ARMOR_CLASS + dexModifier + armorValue, 0, MAX_ARMOR_CLASS);
    }

    public static int getDamageReduction(@NotNull LivingEntity entity) {
        AttributeInstance attributeInstance = entity.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (attributeInstance == null) return 0;
        return Math.clamp((int) attributeInstance.getValue(), 0, MAX_DAMAGE_REDUCTION);
    }

    public static int calculateFinalDamage(int damage, int damageReduction) {
        int reducedDamage = damage - damageReduction;

        return Math.max(1, reducedDamage);
    }

    public static boolean checkHit(LivingEntity attacker, int targetAC) {
        int roll = Dice.FULL.roll();

        if (roll == 20) return true;
        if (roll == 1) return false;

        AbilityScore abilityScore = new AbilityScore(attacker);
        int attackRollBonus = abilityScore.getScoreModifier(AbilityScoreContainer.Types.STRENGTH);

        int totalAttack = roll + attackRollBonus;

        return totalAttack >= targetAC;
    }

}
