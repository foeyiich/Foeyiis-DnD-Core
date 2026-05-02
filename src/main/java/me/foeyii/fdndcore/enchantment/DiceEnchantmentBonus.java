package me.foeyii.fdndcore.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.data.DnDDataMaps;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record DiceEnchantmentBonus(List<DiceEnchantmentEffect> effects) {

    public static final Codec<DiceEnchantmentBonus> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    DiceEnchantmentEffect.CODEC.listOf()
                            .fieldOf("effects")
                            .forGetter(DiceEnchantmentBonus::effects)
            ).apply(instance, DiceEnchantmentBonus::new)
    );

    public DiceEnchantmentBonus(DiceEnchantmentEffect... effect) {
        this(List.of(effect));
    }

    public static @Nullable DiceEnchantmentBonus get(@NotNull Holder<Enchantment> enchantment) {
        return enchantment.getData(DnDDataMaps.DICE_ENCHANTMENT_BONUSES);
    }
}
