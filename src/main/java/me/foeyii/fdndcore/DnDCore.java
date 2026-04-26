package me.foeyii.fdndcore;

import me.foeyii.fdndcore.command.MainCommand;
import me.foeyii.fdndcore.data.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(DnDCore.MODID)
public class DnDCore {

    public static final String MODID = "fdnd_core";

    public DnDCore(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        registerData(modEventBus);
    }

    private void registerData(IEventBus modEventBus) {
        DnDAttachments.register(modEventBus);
        DnDAbilityScoreType.register(modEventBus);
        DnDDamageTypes.register(modEventBus);
        DnDDataComponents.register(modEventBus);
        DnDAttributes.register(modEventBus);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MainCommand.register(event.getDispatcher(), event.getBuildContext());
    }
}
