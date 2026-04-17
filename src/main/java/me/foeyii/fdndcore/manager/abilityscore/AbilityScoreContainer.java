package me.foeyii.fdndcore.manager.abilityscore;

import lombok.Getter;
import me.foeyii.fdndcore.FoeyiisDnDCore;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

@Getter
public class AbilityScoreContainer {

    @Getter
    public enum Types {
        STRENGTH("strength"),
        DEXTERITY("dexterity"),
        CONSTITUTION("constitution"),
        INTELLIGENCE("intelligence"),
        WISDOM("wisdom"),
        CHARISMA("charisma");

        private final String id;

        Types(String s) {
            this.id = s;
        }

    }

    public static final int VALUE_RATE = 1;

    public static final int MAX_VALUE = 20 * VALUE_RATE;
    public static final int MIN_VALUE = 6 * VALUE_RATE;
    public static final int DEFAULT_VALUE = 10 * VALUE_RATE;

    private final EnumMap<Types, Integer> abilities = new EnumMap<>(Types.class);

    public AbilityScoreContainer() {
        abilities.clear();
        abilities.put(Types.STRENGTH, DEFAULT_VALUE);
        abilities.put(Types.DEXTERITY, DEFAULT_VALUE);
        abilities.put(Types.CONSTITUTION, DEFAULT_VALUE);
        abilities.put(Types.INTELLIGENCE, DEFAULT_VALUE);
        abilities.put(Types.WISDOM, DEFAULT_VALUE);
        abilities.put(Types.CHARISMA, DEFAULT_VALUE);
    }

    public void setScore(Types type, int stat) {
        abilities.put(type, clamp(stat));
    }

    public int getScore(Types type) {
        abilities.putIfAbsent(type, DEFAULT_VALUE);
        return abilities.get(type);
    }

    public void setAllScore(int value) {
        for (Types ability : Types.values()) {
            setScore(ability, value);
        }
    }

    public int getScoreModifier(Types type) {
        return calculateModifier(abilities.get(type));
    }

    public static int clamp(int value) {
        if (value > MAX_VALUE) {
            value = MAX_VALUE;
            FoeyiisDnDCore.getLOGGER().warn("Value of " + value + " is greater than " + MAX_VALUE);
        } else if (value < MIN_VALUE) {
            value = MIN_VALUE;
            FoeyiisDnDCore.getLOGGER().warn("Value of " + value + " is less than " + MIN_VALUE);
        }
        return value;
    }

    public static int calculateModifier(int value) {
        return Math.floorDiv((value - 10), 2);
    }

    public static @NotNull AbilityScoreContainer get(@NotNull LivingEntity entity) {
        return entity.getData(AbilityScoreManager.ATTACHMENT_TYPE);
    }

}
