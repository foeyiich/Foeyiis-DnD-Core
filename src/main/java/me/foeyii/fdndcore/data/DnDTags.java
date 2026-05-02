package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;

public final class DnDTags {
    private DnDTags() {
        /* This utility class should not be instantiated */
    }

    public static class Item {
        private Item() {
            /* This utility class should not be instantiated */
        }

        public static final TagKey<net.minecraft.world.item.Item> BLUDGEONING_WEAPONS = createTag("bludgeoning_weapons");
        public static final TagKey<net.minecraft.world.item.Item> PIERCING_WEAPONS = createTag("piercing_weapons");
        public static final TagKey<net.minecraft.world.item.Item> SLASHING_WEAPONS = createTag("slashing_weapons");
    }

    private static TagKey<net.minecraft.world.item.Item> createTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, name));
    }

}
