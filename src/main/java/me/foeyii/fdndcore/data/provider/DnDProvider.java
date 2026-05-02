package me.foeyii.fdndcore.data.provider;

import me.foeyii.fdndcore.DnDCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DnDCore.MODID)
public final class DnDProvider {
    private DnDProvider() {
        /* This utility class should not be instantiated */
    }

    @SubscribeEvent
    static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        DnDBlockTagProvider blockTagProvider = new DnDBlockTagProvider(output, provider, existingFileHelper);

        gen.addProvider(event.includeServer(), new DnDDataMapProvider(output, provider));
        gen.addProvider(event.includeServer(), blockTagProvider);
        gen.addProvider(event.includeServer(), new DnDItemTagProvider(output, provider, blockTagProvider.contentsGetter(), existingFileHelper));
        gen.addProvider(event.includeClient(), new DnDFontProvider(output));
    }

}
