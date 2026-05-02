package me.foeyii.fdndcore.system.damage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.foeyii.fdndcore.data.DnDIcons;
import me.foeyii.fdndcore.data.DnDRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.RegistryFixedCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record DamageType(String translatableName, TextColor color, Optional<String> iconUnicode) {

    public DamageType(String translatableName) {
        this(translatableName, TextColor.fromRgb(0xFFFFFF), Optional.empty());
    }

    public DamageType(String translatableName, TextColor color) {
        this(translatableName, color, Optional.empty());
    }

    public DamageType(String translatableName, int colorHex) {
        this(translatableName, TextColor.fromRgb(colorHex), Optional.empty());
    }

    public DamageType(String translatableName, int colorHex, Optional<String> iconUnicode) {
        this(translatableName, TextColor.fromRgb(colorHex), iconUnicode);
    }

    public @NotNull String name() {
        return Component.translatable(translatableName).getString();
    }

    public @NotNull MutableComponent icon() {
        return DnDIcons.getIcon(iconUnicode.orElse(" "));
    }

    public static final Codec<DamageType> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("translatableName").forGetter(DamageType::translatableName),
                    TextColor.CODEC.fieldOf("color").forGetter(DamageType::color),
                    Codec.STRING.optionalFieldOf("icon").forGetter(DamageType::iconUnicode)
            ).apply(instance, DamageType::new)
    );

    public static final Codec<Holder<DamageType>> HOLDER_CODEC =
            RegistryFixedCodec.create(DnDRegistries.DAMAGE_TYPE);

}