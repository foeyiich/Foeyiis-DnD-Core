package me.foeyii.fdndcore.abilityscore;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

public class AbilityScoreSerializer implements IAttachmentSerializer<CompoundTag, AbilityScoreContainer> {

    @Override
    public AbilityScoreContainer read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        AbilityScoreContainer stats = new AbilityScoreContainer();
        for (AbilityScoreContainer.Types type : AbilityScoreContainer.Types.values()) {
            stats.setScore(type, tag.getInt(type.getId()));
        }
        return stats;
    }

    @Override
    public CompoundTag write(AbilityScoreContainer stats, HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (AbilityScoreContainer.Types type : AbilityScoreContainer.Types.values()) {
            tag.putInt(type.getId(), stats.getScore(type));
        }
        return tag;
    }
}