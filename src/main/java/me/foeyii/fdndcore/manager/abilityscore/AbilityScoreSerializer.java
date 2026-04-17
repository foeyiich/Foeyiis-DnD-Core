package me.foeyii.fdndcore.manager.abilityscore;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

public class AbilityScoreSerializer implements IAttachmentSerializer<CompoundTag, AbilityScoreContainer> {

    @Override
    public AbilityScoreContainer read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        AbilityScoreContainer stats = new AbilityScoreContainer();
        stats.setScore(AbilityScoreContainer.Types.STRENGTH, tag.getInt("strength"));
        stats.setScore(AbilityScoreContainer.Types.DEXTERITY, tag.getInt("dexterity"));
        stats.setScore(AbilityScoreContainer.Types.CONSTITUTION, tag.getInt("constitution"));
        stats.setScore(AbilityScoreContainer.Types.INTELLIGENCE, tag.getInt("intelligence"));
        stats.setScore(AbilityScoreContainer.Types.WISDOM, tag.getInt("wisdom"));
        stats.setScore(AbilityScoreContainer.Types.CHARISMA, tag.getInt("charisma"));
        return stats;
    }

    @Override
    public CompoundTag write(AbilityScoreContainer stats, HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("strength", stats.getScore(AbilityScoreContainer.Types.STRENGTH));
        tag.putInt("dexterity", stats.getScore(AbilityScoreContainer.Types.DEXTERITY));
        tag.putInt("constitution", stats.getScore(AbilityScoreContainer.Types.CONSTITUTION));
        tag.putInt("intelligence", stats.getScore(AbilityScoreContainer.Types.INTELLIGENCE));
        tag.putInt("wisdom", stats.getScore(AbilityScoreContainer.Types.WISDOM));
        tag.putInt("charisma", stats.getScore(AbilityScoreContainer.Types.CHARISMA));
        return tag;
    }
}