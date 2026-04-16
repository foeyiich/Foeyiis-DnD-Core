package me.foeyii.fdndcore.server.config;

import me.foeyii.fdndcore.FConfigBase;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class FConfigItemDamage extends FConfigBase {

    public static final FConfigItemDamage INSTANCE;
    public static final ModConfigSpec SPEC;

    private FConfigItemDamage(ModConfigSpec.Builder builder) {
    }

    static {
        Pair<FConfigItemDamage, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(FConfigItemDamage::new);

        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

}
