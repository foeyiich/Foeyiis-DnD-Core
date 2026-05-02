package me.foeyii.fdndcore.system.abilityscore;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import me.foeyii.fdndcore.data.DnDAbilityScoreType;
import me.foeyii.fdndcore.data.DnDAttachments;
import me.foeyii.fdndcore.system.abilityscore.event.AbilityScoreChangedEvent;
import me.foeyii.fdndcore.utility.DnDUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public final class AbilityScore {

    public static final int VALUE_RATE = 1;
    public static final int MAX_VALUE = 20 * VALUE_RATE;
    public static final int MIN_VALUE = 8 * VALUE_RATE;
    public static final int DEFAULT_VALUE = 10 * VALUE_RATE;

    public static final Codec<AbilityScore> CODEC = Codec.unboundedMap(
            AbilityScoreType.HOLDER_CODEC,
            Codec.INT
    ).xmap(
            rawMap -> {
                AbilityScore container = new AbilityScore();
                container.abilities.putAll(rawMap);
                return container;
            },
            container -> container.abilities
    );

    private final Reference2IntMap<Holder<AbilityScoreType>> abilities = new Reference2IntOpenHashMap<>();

    @Setter
    private @Nullable LivingEntity entity;

    public AbilityScore() {
        // Initialize with default values if registry is available
        // Note: In some contexts (like early loading), registry might not be fully populated
    }

    public void setScore(Holder<AbilityScoreType> type, int score) {
        int oldScore = getScore(type);
        score = clamp(score);
        abilities.put(type, score);
        if (entity != null)
            NeoForge.EVENT_BUS.post(new AbilityScoreChangedEvent(entity, type, oldScore, score));
    }

    public int getScore(Holder<AbilityScoreType> type) {
        int defaultScore = type.value().defaultScore().orElse(DEFAULT_VALUE);
        return abilities.getOrDefault(type, defaultScore);
    }

    public void setAllScore(int score) {
        for (Holder<AbilityScoreType> ability : DnDAbilityScoreType.ABILITY_SCORE_TYPES.getEntries()) {
            setScore(ability, score);
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

    public static @NotNull AbilityScore get(@NotNull LivingEntity entity) {
        AbilityScore abilityScore = entity.getData(DnDAttachments.ABILITY_SCORE);
        if (abilityScore.entity == null)
            abilityScore.entity = entity;
        return abilityScore;
    }

}
