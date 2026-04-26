package me.foeyii.fdndcore.system.abilityscore;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import lombok.Getter;
import me.foeyii.fdndcore.data.DnDAbilityScoreType;
import me.foeyii.fdndcore.data.DnDAttachments;
import me.foeyii.fdndcore.utility.DnDUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class AbilityScoreContainer {

    public static final int VALUE_RATE = 1;
    public static final int MAX_VALUE = 20 * VALUE_RATE;
    public static final int MIN_VALUE = 8 * VALUE_RATE;
    public static final int DEFAULT_VALUE = 10 * VALUE_RATE;

    public static final Codec<AbilityScoreContainer> CODEC = Codec.unboundedMap(
            AbilityScoreType.HOLDER_CODEC,
            Codec.INT
    ).xmap(
            rawMap -> {
                AbilityScoreContainer container = new AbilityScoreContainer();
                container.abilities.putAll(rawMap);
                return container;
            },
            container -> container.abilities
    );

    private final Reference2IntMap<Holder<AbilityScoreType>> abilities = new Reference2IntOpenHashMap<>();

    public AbilityScoreContainer() {
        // Initialize with default values if registry is available
        // Note: In some contexts (like early loading), registry might not be fully populated
    }

    public void setScore(Holder<AbilityScoreType> type, int stat) {
        abilities.put(type, clamp(stat));
    }

    public int getScore(Holder<AbilityScoreType> type) {
        return abilities.getOrDefault(type, type.value().defaultValue().orElse(DEFAULT_VALUE));
    }

    public void setAllScore(int value) {
        for (Holder<AbilityScoreType> ability : DnDAbilityScoreType.ABILITY_SCORE_TYPES.getEntries()) {
            setScore(ability, value);
        }
    }

    public int getScoreModifier(Holder<AbilityScoreType> type) {
        return DnDUtils.calculateModifier(getScore(type), MAX_VALUE);
    }

    public static int clamp(int value) {
        if (value > MAX_VALUE) {
            value = MAX_VALUE;
        } else if (value < MIN_VALUE) {
            value = MIN_VALUE;
        }
        return value;
    }

    public static @NotNull AbilityScoreContainer get(@NotNull LivingEntity entity) {
        return entity.getData(DnDAttachments.ABILITY_SCORE_CONTAINER);
    }

    public List<Holder<AbilityScoreType>> getRegisteredTypes() {
        return new ArrayList<>(DnDAbilityScoreType.ABILITY_SCORE_TYPES.getEntries());
    }

}
