package me.foeyii.fdndcore;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public abstract class FConfigBase {

    private static final String CONFIG_FOLDER = "foeyiis_dnd_core";

    public static void register(ModContainer container, ModConfig.Type type, ModConfigSpec spec, String fileName) {
        container.registerConfig(type, spec, CONFIG_FOLDER + "/" + fileName + ".toml");
    }

}
