package me.foeyii.fdndcore.client.event;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.system.damage.DamageType;
import me.foeyii.fdndcore.system.dice.Dice;
import me.foeyii.fdndcore.utility.DnDDamageResolver;
import me.foeyii.fdndcore.utility.DnDLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.apache.logging.log4j.Logger;

import java.util.List;

@EventBusSubscriber(modid = DnDCore.MODID, value = Dist.CLIENT)
public class TooltipEvents {
    private TooltipEvents() {
        /* This utility class should not be instantiated */
    }

    private static final Logger LOGGER = DnDLogger.getLogger(TooltipEvents.class);

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        LOGGER.info("onItemTooltip: Called");
        ItemStack itemStack = event.getItemStack();
        Player entity = event.getEntity();
        if (entity == null) {
            LOGGER.info("onItemTooltip: Player is null");
            return;
        }

        Dice diceDamage = DnDDamageResolver.resolveEffectiveDiceDamage(itemStack);
        DamageType damageType = DnDDamageResolver.resolveEffectiveDamageType(itemStack).value();
        int attackRollBonus = DnDDamageResolver.resolveAttackRollBonus(itemStack);

        LOGGER.info("onItemTooltip: Data is not null");

        List<Component> tooltips = event.getToolTip();
        int insertIndex = -1;

        for (int i = tooltips.size() - 1; i > 0; i--) {
            Component line = tooltips.get(i);
            if (line.getContents() instanceof TranslatableContents trans &&
                    trans.getKey().startsWith("item.modifiers")) {
                insertIndex = i + 1;
                break;
            }
        }

        if (insertIndex == -1) return;

        Component diceLine = Component.literal(" ")
                .append(diceDamage.toString())
                .append(" ")
                .append(Component.translatable("attribute.name.generic.attack_damage",
                        damageType.icon(),
                        damageType.name()))
                .withStyle(ChatFormatting.DARK_GREEN);
        tooltips.set(insertIndex, diceLine);

//        if (attackRollBonus == 0) return;
//        insertIndex++;
//
//        if (tooltips.get(insertIndex).contains(Component.translatable("attribute.name.generic.attack_speed")))
//            insertIndex++;
//
//        String attackRoll = attackRollBonus > 0 ?
//                "+" + attackRollBonus :
//                String.valueOf(attackRollBonus);
//        Component bonusLine = Component.literal(" ")
//                .append(attackRoll)
//                .append(" ")
//                .append(Component.translatable("attribute.name.generic.attack_roll_bonus"))
//                .withStyle(ChatFormatting.DARK_GREEN);
//        tooltips.add(insertIndex, bonusLine);
    }


}
