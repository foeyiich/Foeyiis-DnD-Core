package me.foeyii.fdndcore.data;

import me.foeyii.fdndcore.DnDCore;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public final class DnDIcons {
    private DnDIcons() {
        /* This utility class should not be instantiated */
    }

    public static final ResourceLocation FONT_RL = ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "icons");

    public static MutableComponent getIcon(String iconString, ResourceLocation fontResourceLocation) {
        return Component.literal(iconString).withStyle(style -> style.withFont(fontResourceLocation));
    }

    public static MutableComponent getIcon(String iconString) {
        return getIcon(iconString, FONT_RL);
    }

    public static class DamageType {
        private DamageType() {
        }

        public static final MutableComponent BLUDGEONING = getIcon("\uE001");
        public static final MutableComponent PIERCING = getIcon("\uE002");
        public static final MutableComponent SLASHING = getIcon("\uE003");

        public static final MutableComponent FIRE = getIcon("\uE004");
        public static final MutableComponent COLD = getIcon("\uE005");
        public static final MutableComponent LIGHTNING = getIcon("\uE006");
        public static final MutableComponent POISON = getIcon("\uE007");
        public static final MutableComponent WITHER = getIcon("\uE008");
        public static final MutableComponent SONIC = getIcon("\uE009");
        public static final MutableComponent FORCE = getIcon("\uE010");

    }

}
