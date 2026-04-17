package me.foeyii.fdndcore.manager.damage;

import lombok.Getter;
import net.neoforged.bus.api.IEventBus;

@Getter
public class DamageManager {
    private DamageManager() {
        /* This utility class should not be instantiated */
    }


    public static void register(IEventBus modEventBus) {

        DamageTypeRegistry.register(modEventBus);
        DamageItemDataComponent.register(modEventBus);


    }

}
