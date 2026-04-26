package me.foeyii.fdndcore.system.abilityscore.event;

import lombok.Getter;
import me.foeyii.fdndcore.system.abilityscore.AbilityScoreType;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.NotNull;

@Getter
public final class AbilityScoreChangedEvent extends Event {

    private final LivingEntity entity;
    private final Holder<AbilityScoreType> type;
    private final int oldValue;
    private final int newValue;

    public AbilityScoreChangedEvent(@NotNull LivingEntity entity,
                                    Holder<AbilityScoreType> type,
                                    int oldValue,
                                    int newValue) {
        this.entity = entity;
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

}
