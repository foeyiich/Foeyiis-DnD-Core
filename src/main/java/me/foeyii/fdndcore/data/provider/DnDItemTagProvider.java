package me.foeyii.fdndcore.data.provider;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.DnDTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class DnDItemTagProvider extends ItemTagsProvider {
    public DnDItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, ExistingFileHelper existingFileHelper) {
        super(output, provider, tagLookup, DnDCore.MODID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(DnDTags.Item.SLASHING_WEAPONS)
                .addTags(ItemTags.SWORDS);

        tag(DnDTags.Item.BLUDGEONING_WEAPONS)
                .addTags(ItemTags.AXES)
                .add(Items.MACE);

        tag(DnDTags.Item.PIERCING_WEAPONS)
                .addTags(ItemTags.PICKAXES)
                .add(Items.BOW, Items.CROSSBOW, Items.TRIDENT);
    }
}
