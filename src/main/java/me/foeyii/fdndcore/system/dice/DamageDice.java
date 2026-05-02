package me.foeyii.fdndcore.system.dice;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.foeyii.fdndcore.system.damage.DamageType;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record DamageDice(@NotNull Dice baseDice, @NotNull Holder<DamageType> damageType) {

    public static final Codec<DamageDice> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Dice.CODEC.fieldOf("base_dice").forGetter(DamageDice::baseDice),
                    DamageType.HOLDER_CODEC.fieldOf("damage_type").forGetter(DamageDice::damageType)
            ).apply(instance, DamageDice::of)
    );

    private record Key(@NotNull Dice dice, @NotNull Holder<DamageType> damageType) {
    }

    private static final Map<Key, DamageDice> CACHES = new Object2ObjectOpenHashMap<>();

    public static DamageDice of(@NotNull Dice dice, @NotNull Holder<DamageType> damageType) {
        return CACHES.computeIfAbsent(new Key(dice, damageType),
                k -> new DamageDice(k.dice(), k.damageType()));
    }

}