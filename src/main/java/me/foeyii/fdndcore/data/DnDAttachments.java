package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.system.abilityscore.AbilityScore;
import me.foeyii.fdndcore.system.abilityscore.AbilityScoreSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class DnDAttachments {
    private DnDAttachments() {
        /* This utility class should not be instantiated */
    }

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DnDCore.MODID);

    public static final Supplier<AttachmentType<AbilityScore>> ABILITY_SCORE =
            DnDAttachments.ATTACHMENT_TYPES.register("ability_score", () -> AttachmentType.builder(AbilityScore::new)
                    .serialize(new AbilityScoreSerializer())
                    .copyOnDeath()
                    .build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
