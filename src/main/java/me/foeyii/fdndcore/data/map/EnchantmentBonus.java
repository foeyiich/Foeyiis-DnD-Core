package me.foeyii.fdndcore.data.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.data.DnDDataMaps;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record EnchantmentBonus(int count, int sides, int modifier, int attackRollBonus) {

    public static final Codec<EnchantmentBonus> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("count", 0).forGetter(EnchantmentBonus::count),
                    Codec.INT.optionalFieldOf("sides", 0).forGetter(EnchantmentBonus::sides),
                    Codec.INT.optionalFieldOf("modifier", 0).forGetter(EnchantmentBonus::modifier),
                    Codec.INT.optionalFieldOf("attack_roll_bonus", 0).forGetter(EnchantmentBonus::attackRollBonus)
            ).apply(instance, EnchantmentBonus::new)
    );

    public static @Nullable EnchantmentBonus get(@NotNull Holder<Enchantment> enchantment) {
        return enchantment.getData(DnDDataMaps.ENCHANTMENT_BONUSES);
    }

}