package me.foeyii.fdndcore.data.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.data.DnDDataMaps;
import me.foeyii.fdndcore.dice.Dice;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record CombatItem(Optional<Dice> dice, int attackRollBonus) {

    public CombatItem(int attackRollBonus) {
        this(Optional.empty(), attackRollBonus);
    }

    public static final Codec<CombatItem> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Dice.CODEC.optionalFieldOf("dice").forGetter(CombatItem::dice),
                    Codec.INT.fieldOf("attack_roll").forGetter(CombatItem::attackRollBonus)
            ).apply(instance, CombatItem::new)
    );

    public static @Nullable CombatItem get(@NotNull Item item) {
        return BuiltInRegistries.ITEM.wrapAsHolder(item).getData(DnDDataMaps.COMBAT_ITEMS);
    }

}