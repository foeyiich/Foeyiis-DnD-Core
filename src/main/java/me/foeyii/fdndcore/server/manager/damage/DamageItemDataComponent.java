package me.foeyii.fdndcore.server.manager.damage;

import com.mojang.serialization.Codec;
import me.foeyii.fdndcore.FoeyiisDnDCore;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class DamageItemDataComponent {
    private DamageItemDataComponent() {
        /* This utility class should not be instantiated */
    }


    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, FoeyiisDnDCore.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> DICE_DAMAGE =
            register("dice_damage", stringBuilder -> stringBuilder.persistent(Codec.STRING));

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderUnaryOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }
}
