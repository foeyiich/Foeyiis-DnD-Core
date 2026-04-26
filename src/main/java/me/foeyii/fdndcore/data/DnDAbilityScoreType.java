package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.system.abilityscore.AbilityScoreType;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class DnDAbilityScoreType {
    private DnDAbilityScoreType() {
        /* This utility class should not be instantiated */
    }

    public static final ResourceKey<Registry<AbilityScoreType>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "ability_score"));

    public static final DeferredRegister<AbilityScoreType> ABILITY_SCORE_TYPES =
            DeferredRegister.create(REGISTRY_KEY, DnDCore.MODID);

    public static final DeferredHolder<AbilityScoreType, AbilityScoreType> STRENGTH =
            ABILITY_SCORE_TYPES.register("strength", () -> new AbilityScoreType(
                    Component.translatable("ability_score.fdnd_core.strength").getString(),
                    Component.translatable("ability_score.fdnd_core.strength.abbr").getString()
            ));

    public static final DeferredHolder<AbilityScoreType, AbilityScoreType> DEXTERITY =
            ABILITY_SCORE_TYPES.register("dexterity", () -> new AbilityScoreType(
                    Component.translatable("ability_score.fdnd_core.dexterity").getString(),
                    Component.translatable("ability_score.fdnd_core.dexterity.abbr").getString()
            ));

    public static final DeferredHolder<AbilityScoreType, AbilityScoreType> CONSTITUTION =
            ABILITY_SCORE_TYPES.register("constitution", () -> new AbilityScoreType(
                    Component.translatable("ability_score.fdnd_core.constitution").getString(),
                    Component.translatable("ability_score.fdnd_core.constitution.abbr").getString()
            ));

    public static final DeferredHolder<AbilityScoreType, AbilityScoreType> INTELLIGENCE =
            ABILITY_SCORE_TYPES.register("intelligence", () -> new AbilityScoreType(
                    Component.translatable("ability_score.fdnd_core.intelligence").getString(),
                    Component.translatable("ability_score.fdnd_core.intelligence.abbr").getString()
            ));

    public static final DeferredHolder<AbilityScoreType, AbilityScoreType> WISDOM =
            ABILITY_SCORE_TYPES.register("wisdom", () -> new AbilityScoreType(
                    Component.translatable("ability_score.fdnd_core.wisdom").getString(),
                    Component.translatable("ability_score.fdnd_core.wisdom.abbr").getString()
            ));

    public static final DeferredHolder<AbilityScoreType, AbilityScoreType> CHARISMA =
            ABILITY_SCORE_TYPES.register("charisma", () -> new AbilityScoreType(
                    Component.translatable("ability_score.fdnd_core.charisma").getString(),
                    Component.translatable("ability_score.fdnd_core.charisma.abbr").getString()
            ));

    public static void register(IEventBus bus) {
        ABILITY_SCORE_TYPES.register(bus);
        ABILITY_SCORE_TYPES.makeRegistry(builder ->
                new RegistryBuilder<>(REGISTRY_KEY).sync(true));
    }

}
