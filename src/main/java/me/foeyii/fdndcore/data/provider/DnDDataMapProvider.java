package me.foeyii.fdndcore.data.provider;

import me.foeyii.fdndcore.data.DnDDataMaps;
import me.foeyii.fdndcore.data.provider.subprovider.DnDDiceEnchantmentBonusesSubProvider;
import me.foeyii.fdndcore.data.provider.subprovider.DnDWeaponPropertiesSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class DnDDataMapProvider extends DataMapProvider {
    public DnDDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        DnDWeaponPropertiesSubProvider.register(builder(DnDDataMaps.WEAPON_PROPERTIES));
        DnDDiceEnchantmentBonusesSubProvider.register(builder(DnDDataMaps.DICE_ENCHANTMENT_BONUSES));
    }
}
