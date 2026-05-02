package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.system.damage.DamageType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.Optional;

public final class DnDDamageTypes {
    private DnDDamageTypes() {
    }


    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(DnDRegistries.DAMAGE_TYPE, DnDCore.MODID);

    public static final DeferredHolder<DamageType, DamageType> UNKNOWN = DAMAGE_TYPES.register("unknown",
            () -> new DamageType("Unknown", 0xFF1100));


    public static class Physical {
        private Physical() {
        }

        public static void init() {
        }

        public static final DeferredHolder<DamageType, DamageType> BLUDGEONING = DAMAGE_TYPES.register("physical.bludgeoning",
                () -> new DamageType("damage_type.fdnd_core.physical.bludgeoning", 0xFFFFF, Optional.of(DnDIcons.DamageType.BLUDGEONING.getString())));

        public static final DeferredHolder<DamageType, DamageType> PIERCING = DAMAGE_TYPES.register("physical.piercing",
                () -> new DamageType("damage_type.fdnd_core.physical.piercing", 0xFFFFFF, Optional.of(DnDIcons.DamageType.PIERCING.getString())));

        public static final DeferredHolder<DamageType, DamageType> SLASHING = DAMAGE_TYPES.register("physical.slashing",
                () -> new DamageType("damage_type.fdnd_core.physical.slashing", 0xFFFFFF, Optional.of(DnDIcons.DamageType.SLASHING.getString())));
    }

    public static class Elemental {
        private Elemental() {
        }

        public static void init() {
        }

        public static final DeferredHolder<DamageType, DamageType> FIRE = DAMAGE_TYPES.register("elemental.fire",
                () -> new DamageType("damage_type.fdnd_core.elemental.fire", 0xBF4600));
        public static final DeferredHolder<DamageType, DamageType> COLD = DAMAGE_TYPES.register("elemental.cold",
                () -> new DamageType("damage_type.fdnd_core.elemental.cold", 0x5555FF));
        public static final DeferredHolder<DamageType, DamageType> POISON = DAMAGE_TYPES.register("elemental.poison",
                () -> new DamageType("damage_type.fdnd_core.elemental.poison", 0x00AA00));
        public static final DeferredHolder<DamageType, DamageType> WITHER = DAMAGE_TYPES.register("elemental.wither",
                () -> new DamageType("damage_type.fdnd_core.elemental.wither", 0x1C1C1C));
        public static final DeferredHolder<DamageType, DamageType> LIGHTNING = DAMAGE_TYPES.register("elemental.lightning",
                () -> new DamageType("damage_type.fdnd_core.elemental.lightning", 0x2A5DCC));
        public static final DeferredHolder<DamageType, DamageType> SONIC = DAMAGE_TYPES.register("elemental.sonic",
                () -> new DamageType("damage_type.fdnd_core.elemental.sonic", 0x00FFD0));
        public static final DeferredHolder<DamageType, DamageType> FORCE = DAMAGE_TYPES.register("elemental.force",
                () -> new DamageType("damage_type.fdnd_core.elemental.force", 0x9400D3));

    }

    public static void register(IEventBus bus) {
        DAMAGE_TYPES.register(bus);

        Physical.init();
        Elemental.init();

        DAMAGE_TYPES.makeRegistry(builder -> new RegistryBuilder<>(DnDRegistries.DAMAGE_TYPE)
                .sync(true)
                .maxId(256));
    }
}