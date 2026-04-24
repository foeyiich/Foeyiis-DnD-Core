package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.logic.damage.DamageType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class DnDDamageTypes {
    private DnDDamageTypes() {
    }

    public static final ResourceKey<Registry<DamageType>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "damage_types"));

    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(REGISTRY_KEY, DnDCore.MODID);

    public static final DeferredHolder<DamageType, DamageType> PHYSICAL = DAMAGE_TYPES.register("physical",
            () -> new DamageType("Physical", 0xFFFFFF));
    public static final DeferredHolder<DamageType, DamageType> FIRE = DAMAGE_TYPES.register("fire",
            () -> new DamageType("Fire", 0xFF5555));
    public static final DeferredHolder<DamageType, DamageType> ICE = DAMAGE_TYPES.register("ice",
            () -> new DamageType("Ice", 0x5555FF));
    public static final DeferredHolder<DamageType, DamageType> POISON = DAMAGE_TYPES.register("poison",
            () -> new DamageType("Poison", 0x00AA00));

    public static void register(IEventBus bus) {
        DAMAGE_TYPES.register(bus);
        DAMAGE_TYPES.makeRegistry(builder -> new RegistryBuilder<>(REGISTRY_KEY)
                .sync(true)
                .maxId(256));
    }
}