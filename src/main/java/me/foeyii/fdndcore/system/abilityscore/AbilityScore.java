package me.foeyii.fdndcore.system.abilityscore;

import lombok.Getter;
import me.foeyii.fdndcore.system.abilityscore.event.AbilityScoreChangedEvent;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

@Getter
public class AbilityScore {

    private final LivingEntity entity;
    private final AbilityScoreContainer container;

    public AbilityScore(@NotNull LivingEntity entity) {
        this.entity = entity;
        this.container = AbilityScoreContainer.get(entity);
    }

    public void setScore(Holder<AbilityScoreType> type, int score) {
        int oldScore = container.getScore(type);
        container.setScore(type, score);
        NeoForge.EVENT_BUS.post(new AbilityScoreChangedEvent(entity, type, oldScore, score));
    }

    public void setAllScore(int score) {
        container.setAllScore(score);
    }

    public int getScore(Holder<AbilityScoreType> type) {
        return container.getScore(type);
    }

    public int getScoreModifier(Holder<AbilityScoreType> type) {
        return container.getScoreModifier(type);
    }

}
