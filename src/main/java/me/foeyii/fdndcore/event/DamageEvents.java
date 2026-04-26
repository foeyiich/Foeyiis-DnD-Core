package me.foeyii.fdndcore.event;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.DnDAbilityScoreType;
import me.foeyii.fdndcore.data.DnDDamageTypes;
import me.foeyii.fdndcore.system.abilityscore.AbilityScoreContainer;
import me.foeyii.fdndcore.system.damage.DamagePool;
import me.foeyii.fdndcore.system.damage.DamageType;
import me.foeyii.fdndcore.system.dice.Dice;
import me.foeyii.fdndcore.utility.DnDItemUtils;
import me.foeyii.fdndcore.utility.DnDLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = DnDCore.MODID)
public class DamageEvents {
    private DamageEvents() {
        /* This utility class should not be instantiated */
    }

    private static final Map<UUID, Map<DamageType, Integer>> damageRollsCache = new WeakHashMap<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void onDamagePre(LivingIncomingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;

        DamagePool pool = new DamagePool();
        RandomSource random = attacker.getRandom();
        ItemStack itemStack = attacker.getMainHandItem();

        int baseDamage = (int) attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        baseDamage = Math.clamp(
                (long) baseDamage + AbilityScoreContainer.get(attacker).getScoreModifier(DnDAbilityScoreType.STRENGTH),
                -5,
                5
        );

        if (!itemStack.is(Items.AIR)) {
            baseDamage /= 2;
        }
        pool.addDice(DnDDamageTypes.PHYSICAL.get(), DnDItemUtils.getDice(itemStack, attacker.level()));

        pool.addDice(DnDDamageTypes.PHYSICAL.get(), Dice.from(baseDamage));

        pool.rollAll(random);

        event.setAmount(pool.getTotalDamage());

        damageRollsCache.put(attacker.getUUID(), pool.getResults());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;

        Map<DamageType, Integer> results = damageRollsCache.remove(attacker.getUUID());

        if (results != null && !results.isEmpty()) {
            var component = Component.literal("");
            component.append("§l[HIT] §r");

            results.forEach((type, value) -> {
                TextColor color = type.color();
                component
                        .append("| ").withStyle(ChatFormatting.RESET)
                        .append(Component.literal(String.valueOf(value)).withStyle(style -> style.withColor(color)))
                        .append(" ")
                        .append(Component.literal(type.name()).withStyle(style -> style.withColor(color)));
            });

            DnDLogger.getLogger(DamageEvents.class).info(component.getString());
            attacker.sendSystemMessage(component);

        }
    }

}
