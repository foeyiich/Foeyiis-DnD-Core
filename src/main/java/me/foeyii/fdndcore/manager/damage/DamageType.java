package me.foeyii.fdndcore.manager.damage;

import net.minecraft.network.chat.TextColor;

public class DamageType {
    private final String name;
    private final TextColor color;

    public DamageType(String name, int colorHex) {
        this.name = name;
        this.color = TextColor.fromRgb(colorHex);
    }

    public String getName() {
        return name;
    }

    public TextColor getColor() {
        return color;
    }
}
