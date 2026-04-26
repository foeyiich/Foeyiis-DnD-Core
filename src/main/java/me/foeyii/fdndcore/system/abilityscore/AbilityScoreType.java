package me.foeyii.fdndcore.system.abilityscore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.data.DnDAbilityScoreType;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record AbilityScoreType(String displayName, String abbreviation, String description,
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
