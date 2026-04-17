package me.foeyii.fdndcore.manager.abilityscore;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class AbilityScoreEventHandler {

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        AbilityScoreManager.syncAttributes(player);
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent event) {
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

}
