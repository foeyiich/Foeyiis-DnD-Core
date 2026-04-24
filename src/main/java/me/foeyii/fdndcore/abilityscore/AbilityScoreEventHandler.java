package me.foeyii.fdndcore.abilityscore;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.abilityscore.event.AbilityScoreChangedEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = DnDCore.MODID)
public class AbilityScoreEventHandler {
    private AbilityScoreEventHandler() {
        /* This utility class should not be instantiated */
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        AbilityScoreManager.syncAttributes(player);
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide())
            return;
        if (event.getEntity() instanceof Mob mob) {
            boolean isHostile = mob instanceof net.minecraft.world.entity.monster.Enemy;
            boolean isNeutral = mob instanceof net.minecraft.world.entity.NeutralMob;

            if (isHostile || isNeutral) {
                AbilityScoreManager.syncAttributes(mob);
            }
        }
    }

    @SubscribeEvent
    public static void onAbilityScoreChange(AbilityScoreChangedEvent event) {
        LivingEntity entity = event.getEntity();
        AbilityScoreManager.syncAttributes(entity);
    }

}
