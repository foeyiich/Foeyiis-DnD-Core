package me.foeyii.fdndcore.system.abilityscore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.data.DnDAbilityScoreType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record AbilityScoreType(String translatableDisplayName, String translatableAbbreviation,
                               String translatableDescription,
                               Optional<ResourceLocation> icon, Optional<Integer> defaultValue) {

    public AbilityScoreType(String displayName) {
        this(displayName, displayName.substring(0, 3), "", Optional.empty(), Optional.of(0));
    }

    public AbilityScoreType(String displayName, String abbreviation) {
        this(displayName, abbreviation, "", Optional.empty(), Optional.of(0));
    }

    public AbilityScoreType(String displayName, String abbreviation, String description) {
        this(displayName, abbreviation, description, Optional.empty(), Optional.of(0));
    }

    public AbilityScoreType(String displayName, String abbreviation, String description, Optional<ResourceLocation> icon) {
        this(displayName, abbreviation, description, icon, Optional.of(0));
    }

    public @NotNull String displayName() {
        return Component.translatable(translatableDisplayName).getString();
    }

    public @NotNull String abbreviation() {
        return Component.translatable(translatableAbbreviation).getString();
    }

    public @NotNull String description() {
        return Component.translatable(translatableDescription).getString();
    }

    public static final Codec<AbilityScoreType> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(AbilityScoreType::displayName),
                    Codec.STRING.fieldOf("abbreviation").forGetter(AbilityScoreType::abbreviation),
                    Codec.STRING.fieldOf("description").forGetter(AbilityScoreType::description),
                    ResourceLocation.CODEC.optionalFieldOf("icon").forGetter(AbilityScoreType::icon),
                    Codec.INT.optionalFieldOf("default_value").forGetter(AbilityScoreType::defaultValue)
            ).apply(instance, AbilityScoreType::new)
    );

    public static final Codec<Holder<AbilityScoreType>> HOLDER_CODEC =
            RegistryFixedCodec.create(DnDAbilityScoreType.REGISTRY_KEY);

}
