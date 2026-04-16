package me.foeyii.fdndcore.client;

import me.foeyii.fdndcore.FoeyiisDnDCore;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventBusSubscriber(modid = FoeyiisDnDCore.MODID, value = Dist.CLIENT)
public class FoeyiisDnDClient {

    public static final Logger logger = LoggerFactory.getLogger(FoeyiisDnDClient.class);

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new ClientItemTooltipHandler());
    }

}
