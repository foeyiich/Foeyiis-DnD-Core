package me.foeyii.fdndcore.event;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.DnDAbilityScoreType;
import me.foeyii.fdndcore.system.abilityscore.AbilityScore;
import me.foeyii.fdndcore.system.abilityscore.AbilityScoreContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = DnDCore.MODID)
public class TradeEvents {
    private TradeEvents() {
        /* This utility class should not be instantiated */
    }

    private static final Int2IntOpenHashMap charismaSpecialPriceDiff = new Int2IntOpenHashMap();

    static {
        for (int i = AbilityScoreContainer.MIN_VALUE; i <= AbilityScoreContainer.MAX_VALUE; i++) {
            charismaSpecialPriceDiff.put(i, calculateSpecialPriceDiffFromMod(i));
        }
    }

    @SubscribeEvent
    static void onVillagerTrade(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getTarget() instanceof Villager villager))
            return;

        Player player = event.getEntity();
        AbilityScore abilityScore = new AbilityScore(player);
        int charismaModifier = abilityScore.getScoreModifier(DnDAbilityScoreType.CHARISMA);
        int specialPriceDiff = charismaSpecialPriceDiff.getOrDefault(
                charismaModifier,
                calculateSpecialPriceDiffFromMod(charismaModifier)
        );

        for (MerchantOffer offer : villager.getOffers()) {
            offer.addToSpecialPriceDiff(specialPriceDiff);
        }
    }

    private static int calculateSpecialPriceDiffFromMod(int charismaModifier) {
        if (charismaModifier == 0)
            return 0;
        int specialPriceDiff = -charismaModifier;
        if (charismaModifier < 0)
            specialPriceDiff = Math.abs(charismaModifier) * 3;
        return specialPriceDiff;
    }

}
