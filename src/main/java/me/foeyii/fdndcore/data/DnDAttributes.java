package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class DnDAttributes {
    private DnDAttributes() {
    }

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(
            BuiltInRegistries.ATTRIBUTE, DnDCore.MODID
    );

    public static final DeferredHolder<Attribute, Attribute> ATTACK_ROLL_BONUS = ATTRIBUTES.register("generic.attack_roll_bonus", () -> new RangedAttribute(
            "attribute.name.generic.attack_roll_bonus",
            0,
            -10000,
            10000
    ));

    public static void register(IEventBus bus) {
        ATTRIBUTES.register(bus);
    }

}
