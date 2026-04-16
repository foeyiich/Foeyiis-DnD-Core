package me.foeyii.fdndcore.client.config;

import me.foeyii.fdndcore.FConfigBase;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class FConfigClient extends FConfigBase {

    public static final FConfigClient INSTANCE;
    public static final ModConfigSpec SPEC;

    public static final Map<String, Map<String, ModConfigSpec.ConfigValue<?>>> configMap = new HashMap<>();

    private FConfigClient(ModConfigSpec.Builder builder) {

        builder.push("dice-damage");
        builder.pop();

    }

    static {
        Pair<FConfigClient, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(FConfigClient::new);

        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

}
