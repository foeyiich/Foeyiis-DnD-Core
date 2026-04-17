package me.foeyii.fdndcore.manager.abilityscore;

import lombok.Getter;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Getter
public class AbilityScore {

    private final LivingEntity entity;
    private final AbilityScoreContainer container;

    public AbilityScore(@NotNull LivingEntity entity) {
        this.entity = entity;
        this.container = AbilityScoreContainer.get(entity);
    }

    public void setScore(AbilityScoreContainer.Types type, int score) {
        container.setScore(type, score);
        AbilityScoreManager.syncAttribute(entity, type);
    }

    public void setAllScore(int score) {
        container.setAllScore(score);
        AbilityScoreManager.syncAttributes(entity);
    }

    public int getScore(AbilityScoreContainer.Types type) {
        return container.getScore(type);
    }

    public int getScoreModifier(AbilityScoreContainer.Types type) {
        return container.getScoreModifier(type);
    }

}
