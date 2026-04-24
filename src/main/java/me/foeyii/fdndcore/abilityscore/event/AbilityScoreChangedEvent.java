package me.foeyii.fdndcore.abilityscore.event;

import lombok.Getter;
import me.foeyii.fdndcore.abilityscore.AbilityScoreContainer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.NotNull;

@Getter
public final class AbilityScoreChangedEvent extends Event {

    private final LivingEntity entity;
    private final AbilityScoreContainer.Types scoreType;
    private final int oldValue;
    private final int newValue;

    public AbilityScoreChangedEvent(@NotNull LivingEntity entity,
                                    AbilityScoreContainer.Types scoreType,
                                    int oldValue,
                                    int newValue) {
        this.entity = entity;
        this.scoreType = scoreType;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

}
