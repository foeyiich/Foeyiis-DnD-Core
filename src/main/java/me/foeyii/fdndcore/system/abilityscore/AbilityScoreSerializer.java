package me.foeyii.fdndcore.system.abilityscore;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public class AbilityScoreSerializer implements IAttachmentSerializer<Tag, AbilityScore> {

    @Override
    public AbilityScore read(IAttachmentHolder holder, Tag tag, HolderLookup.Provider provider) {
        return AbilityScore.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial(error -> {

                })
                .orElseGet(AbilityScore::new);
    }

    @Override
    public @Nullable Tag write(AbilityScore stats, HolderLookup.Provider provider) {
        return AbilityScore.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), stats)
                .resultOrPartial(error -> {
                })
                .orElse(null);
    }
}
