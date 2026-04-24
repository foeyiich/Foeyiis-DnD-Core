package me.foeyii.fdndcore.manager.abilityscore;

import lombok.Getter;
import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.utility.DnDUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
        private final MutableComponent component;

        Types(String s) {
            this.id = s;
            this.component = Component.translatable("fdndcore.ability_score.name." + s);
        }

    }

    public static final int VALUE_RATE = 1;

    public static final int MAX_VALUE = 20 * VALUE_RATE;
    public static final int MIN_VALUE = 8 * VALUE_RATE;
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
        return DnDUtils.calculateModifier(abilities.get(type), MAX_VALUE);
    }

    public static int clamp(int value) {
        if (value > MAX_VALUE) {
            DnDCore.LOGGER.warn("Value of {} is greater than {}", value, MAX_VALUE);
            value = MAX_VALUE;
        } else if (value < MIN_VALUE) {
            DnDCore.LOGGER.warn("Value of {} is less than {}", value, MIN_VALUE);
            value = MIN_VALUE;
        }
        return value;
    }

    public static @NotNull AbilityScoreContainer get(@NotNull LivingEntity entity) {
        return entity.getData(AbilityScoreManager.ATTACHMENT_TYPE);
    }

}
