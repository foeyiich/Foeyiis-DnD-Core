package me.foeyii.fdndcore.server.manager.damage;

import me.foeyii.fdndcore.FoeyiisDnDCore;
import me.foeyii.fdndcore.server.manager.abilityscore.AbilityScoreContainer;
import me.foeyii.fdndcore.server.manager.dice.Dice;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class DamageStrengthHandler {

    private final Map<UUID, Map<DamageType, Integer>> damageRollsCache = new WeakHashMap<>();

    @SubscribeEvent
    public void onDamagePre(LivingDamageEvent.Pre event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;

        DamagePool pool = new DamagePool();
        RandomSource random = attacker.getRandom();
        ItemStack itemStack = attacker.getMainHandItem();

        int baseDamage = (int) attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        if (attacker instanceof ServerPlayer player) {
            baseDamage = Math.clamp(
                    baseDamage + AbilityScoreContainer.get(player).getScoreModifier(AbilityScoreContainer.Types.STRENGTH),
                    0,
                    5
            );
        }

        if (!itemStack.is(Items.AIR)) {
            baseDamage /= 2;
        }
        pool.addDice(DamageTypeRegistry.PHYSICAL.get(), DamageItemManager.getDiceFinalDamage(itemStack, attacker.level()));

        pool.addDice(DamageTypeRegistry.PHYSICAL.get(), Dice.calculateFromInt(baseDamage));

        pool.rollAll(random);

        event.setNewDamage(pool.getTotalDamage());

        damageRollsCache.put(attacker.getUUID(), pool.getResults());
    }

    @SubscribeEvent
    public void onDamagePost(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;

        Map<DamageType, Integer> results = damageRollsCache.remove(attacker.getUUID());

        if (results != null && !results.isEmpty()) {
            var component = Component.literal("");
            component.append("§l[HIT] §r");

            results.forEach((type, value) -> {
                TextColor color = type.getColor();
                component
                        .append("| ").withStyle(ChatFormatting.RESET)
                        .append(Component.literal(String.valueOf(value)).withStyle(style -> style.withColor(color)))
                        .append(" ")
                        .append(Component.literal(type.getName()).withStyle(style -> style.withColor(color)));
            });

            FoeyiisDnDCore.getLOGGER().info(component.getString());
            attacker.sendSystemMessage(component);

        }
    }

}
