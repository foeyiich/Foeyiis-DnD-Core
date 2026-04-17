package me.foeyii.fdndcore;

import com.mojang.logging.LogUtils;
import lombok.Getter;
import me.foeyii.fdndcore.command.MainCommand;
import me.foeyii.fdndcore.config.FConfigItemDamage;
import me.foeyii.fdndcore.manager.abilityscore.AbilityScoreManager;
import me.foeyii.fdndcore.manager.damage.DamageManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

@Mod(FoeyiisDnDCore.MODID)
public class FoeyiisDnDCore {

    public static final String MODID = "fdnd_core";

    @Getter
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public FoeyiisDnDCore(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        AbilityScoreManager.register(modEventBus);
        DamageManager.register(modEventBus);

        FConfigBase.register(modContainer, ModConfig.Type.SERVER, FConfigItemDamage.SPEC, "item-damage");

        ATTACHMENT_TYPES.register(modEventBus);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code

    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MainCommand.register(event.getDispatcher());
    }
}
