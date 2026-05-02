package me.foeyii.fdndcore.data.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.data.DnDDamageTypes;
import me.foeyii.fdndcore.data.DnDDataMaps;
import me.foeyii.fdndcore.system.damage.DamageType;
import me.foeyii.fdndcore.system.dice.DamageDice;
import me.foeyii.fdndcore.system.dice.Dice;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record WeaponProperties(
        Optional<Dice> dice,
        Optional<Holder<DamageType>> overrideType,
        Optional<Integer> attackRollBonus,
        Optional<List<DamageDice>> additionalDamage
) {

    public WeaponProperties(
            @Nullable Dice dice,
            @Nullable Holder<DamageType> overrideType,
            int attackRollBonus,
            @Nullable List<DamageDice> additionalDamage
    ) {
        this(Optional.ofNullable(dice), Optional.ofNullable(overrideType), Optional.of(attackRollBonus), Optional.ofNullable(additionalDamage));
    }

    public DamageDice damageDice() {
        return DamageDice.of(dice.orElse(Dice.EMPTY), overrideType.orElse(DnDDamageTypes.UNKNOWN));
    }

    public static final WeaponProperties EMPTY = new WeaponProperties(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );

    public static final Codec<WeaponProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Dice.CODEC.optionalFieldOf("baseDice").forGetter(WeaponProperties::dice),
                    DamageType.HOLDER_CODEC.optionalFieldOf("override_type").forGetter(WeaponProperties::overrideType),
                    Codec.INT.optionalFieldOf("attack_roll").forGetter(WeaponProperties::attackRollBonus),
                    DamageDice.CODEC.listOf().optionalFieldOf("additional_dice").forGetter(WeaponProperties::additionalDamage)
            ).apply(instance, WeaponProperties::new)
    );

    public static WeaponProperties get(Holder<Item> holder) {
        WeaponProperties data = holder.getData(DnDDataMaps.WEAPON_PROPERTIES);
        return data != null ? data : EMPTY;
    }

}