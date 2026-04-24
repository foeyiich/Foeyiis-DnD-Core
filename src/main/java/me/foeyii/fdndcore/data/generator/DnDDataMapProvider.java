package me.foeyii.fdndcore.data.generator;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.DnDDataMaps;
import me.foeyii.fdndcore.data.map.CombatItem;
import me.foeyii.fdndcore.manager.dice.Dice;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DnDCore.MODID)
public class DnDDataMapProvider extends DataMapProvider {
    protected DnDDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        gatherCombatItems();
    }

    @SubscribeEvent
    protected static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new DnDDataMapProvider(output, provider));
    }

    private void gatherCombatItems() {
        builder(DnDDataMaps.COMBAT_ITEMS)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.WOODEN_SWORD),
                        new CombatItem(
                                Optional.of(new Dice(1, 2, 1)),
                                1
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.STONE_SWORD),
                        new CombatItem(
                                Optional.of(new Dice(1, 4)),
                                1
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.GOLDEN_SWORD),
                        new CombatItem(
                                Optional.of(new Dice(1, 2, 1)),
                                3
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_SWORD),
                        new CombatItem(
                                Optional.of(new Dice(1, 6)),
                                1
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND_SWORD),
                        new CombatItem(
                                Optional.of(new Dice(1, 6, 1)),
                                1
                        ), false
                )
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.NETHERITE_SWORD),
                        new CombatItem(
                                Optional.of(new Dice(1, 8)),
                                1
                        ), false
                )
        ;
    }
}
