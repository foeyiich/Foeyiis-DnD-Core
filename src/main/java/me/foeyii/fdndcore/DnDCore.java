package me.foeyii.fdndcore;

import com.mojang.logging.LogUtils;
import me.foeyii.fdndcore.command.MainCommand;
import me.foeyii.fdndcore.data.DnDAttributes;
import me.foeyii.fdndcore.data.DnDDamageTypes;
import me.foeyii.fdndcore.data.DnDDataComponents;
import me.foeyii.fdndcore.manager.abilityscore.AbilityScoreManager;
import me.foeyii.fdndcore.manager.damage.DamageEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

@Mod(DnDCore.MODID)
public class DnDCore {

    public static final String MODID = "fdnd_core";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public DnDCore(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        AbilityScoreManager.register(modEventBus);
        registerData(modEventBus);

        ATTACHMENT_TYPES.register(modEventBus);

    }

    private void registerData(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(DamageEventHandler.class);
        DnDDamageTypes.register(modEventBus);
        DnDDataComponents.register(modEventBus);
        DnDAttributes.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code

    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MainCommand.register(event.getDispatcher());
    }
}
