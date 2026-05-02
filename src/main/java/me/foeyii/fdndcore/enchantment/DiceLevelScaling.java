package me.foeyii.fdndcore.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.system.dice.Dice;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record DiceLevelScaling(
        LevelBasedValue count,
        LevelBasedValue sides,
        LevelBasedValue modifier
) {
    public static final Codec<DiceLevelScaling> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("count").forGetter(DiceLevelScaling::count),
                    LevelBasedValue.CODEC.fieldOf("sides").forGetter(DiceLevelScaling::sides),
                    LevelBasedValue.CODEC.optionalFieldOf("modifier", LevelBasedValue.constant(0))
                            .forGetter(DiceLevelScaling::modifier)
            ).apply(instance, DiceLevelScaling::new)
    );

    public Dice forLevel(int level) {
        return Dice.of(
                (int) count.calculate(level),
                (int) sides.calculate(level),
                (int) modifier.calculate(level)
        );
    }
}
