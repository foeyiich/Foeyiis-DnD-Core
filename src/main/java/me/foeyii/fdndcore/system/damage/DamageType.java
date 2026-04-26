package me.foeyii.fdndcore.system.damage;

import net.minecraft.network.chat.TextColor;

public record DamageType(String name, TextColor color) {

    public DamageType(String name, int colorHex) {
        this(name, TextColor.fromRgb(colorHex));
    }

}