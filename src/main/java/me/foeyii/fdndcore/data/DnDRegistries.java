package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.system.abilityscore.AbilityScoreType;
import me.foeyii.fdndcore.system.damage.DamageType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class DnDRegistries {
    private DnDRegistries() {
        /* This utility class should not be instantiated */
    }

    public static final ResourceKey<Registry<AbilityScoreType>> ABILITY_SCORE =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "ability_score"));

    public static final ResourceKey<Registry<DamageType>> DAMAGE_TYPE =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "damage_type"));
}
