package me.foeyii.fdndcore.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.system.damage.DamageType;
import me.foeyii.fdndcore.system.dice.Dice;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record DiceEnchantmentEffect(
        DiceLevelScaling scaling,
        Optional<Holder<DamageType>> damageType,
        Optional<TagKey<EntityType<?>>> targetTag
) {
    public static final Codec<DiceEnchantmentEffect> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    DiceLevelScaling.CODEC.fieldOf("scaling").forGetter(DiceEnchantmentEffect::scaling),
                    DamageType.HOLDER_CODEC.optionalFieldOf("damage_type")
                            .forGetter(DiceEnchantmentEffect::damageType),
                    TagKey.hashedCodec(Registries.ENTITY_TYPE).optionalFieldOf("target_tag")
                            .forGetter(DiceEnchantmentEffect::targetTag)
            ).apply(instance, DiceEnchantmentEffect::new)
    );

    public DiceEnchantmentEffect(DiceLevelScaling scaling) {
        this(scaling, Optional.empty(), Optional.empty());
    }

    public DiceEnchantmentEffect(DiceLevelScaling scaling, Optional<Holder<DamageType>> damageType) {
        this(scaling, damageType, Optional.empty());
    }

    public boolean appliesTo(@NotNull LivingEntity target) {
        return targetTag.isEmpty() || target.getType().is(targetTag.get());
    }

    public @NotNull Dice getDice(int level) {
        return scaling.forLevel(level);
    }
}
