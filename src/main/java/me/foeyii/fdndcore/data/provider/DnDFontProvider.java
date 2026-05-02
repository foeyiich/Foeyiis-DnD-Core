package me.foeyii.fdndcore.data.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.DnDDamageTypes;
import me.foeyii.fdndcore.data.DnDIcons;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class DnDFontProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public DnDFontProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "font");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        JsonObject fontJson = new JsonObject();
        JsonArray providers = new JsonArray();

        registerEmoji(
                providers,
                DnDDamageTypes.Physical.BLUDGEONING.getId().getPath(),
                DnDIcons.DamageType.BLUDGEONING.getString()
        );
        registerEmoji(
                providers,
                DnDDamageTypes.Physical.PIERCING.getId().getPath(),
                DnDIcons.DamageType.PIERCING.getString()
        );
        registerEmoji(
                providers,
                DnDDamageTypes.Physical.SLASHING.getId().getPath(),
                DnDIcons.DamageType.SLASHING.getString()
        );

        fontJson.add("providers", providers);
        return DataProvider.saveStable(output, fontJson, pathProvider.json(DnDIcons.FONT_RL));
    }

    private JsonObject createBitmapProvider(ResourceLocation file, int ascent, int height, String... chars) {
        ascent = ascent <= 0 ? 1 : ascent;
        height = height <= 0 ? 1 : height;
        JsonObject provider = new JsonObject();
        provider.addProperty("type", "bitmap");
        provider.addProperty("file", file.toString());
        provider.addProperty("ascent", ascent);
        provider.addProperty("height", height);
        JsonArray charArray = new JsonArray();
        for (String c : chars) charArray.add(c);
        provider.add("chars", charArray);
        return provider;
    }

    @Override
    public @NotNull String getName() {
        return "DnD Icons Font Provider";
    }

    private void registerEmoji(JsonArray providers, String fileName, int ascent, int height, @NotNull String... chars) {
        providers.add(createBitmapProvider(
                ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "font/" + fileName + "_icon.png"),
                ascent, height,
                chars
        ));
    }

    private void registerEmoji(JsonArray providers, String fileName, @NotNull String... chars) {
        registerEmoji(providers, fileName, 8, 8, chars);
    }

}
