package me.foeyii.fdndcore.data;

import com.mojang.serialization.Codec;
import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.system.damage.DamageType;
import me.foeyii.fdndcore.system.dice.Dice;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public final class DnDDataComponents {
    private DnDDataComponents() {
        /* This utility class should not be instantiated */
    }

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, DnDCore.MODID);

    public static class Item {
        public static final DeferredHolder<DataComponentType<?>, DataComponentType<Dice>> DICE_DAMAGE_OVERRIDE =
                register("dice_damage_override", stringBuilder -> stringBuilder.persistent(Dice.CODEC));

        public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<DamageType>>> DAMAGE_TYPE_OVERRIDE =
                register("damage_type_override", stringBuilder -> stringBuilder.persistent(DamageType.HOLDER_CODEC));

        public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ATTACK_ROLL_OVERRIDE =
                register("damage_type_override", stringBuilder -> stringBuilder.persistent(Codec.INT));

        private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator) {
            return DATA_COMPONENT_TYPES.register(name, () -> builderUnaryOperator.apply(DataComponentType.builder()).build());
        }
    }

    public static void register(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }
}
