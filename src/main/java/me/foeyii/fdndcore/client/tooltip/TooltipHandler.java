package me.foeyii.fdndcore.client.tooltip;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.dice.Dice;
import me.foeyii.fdndcore.utility.DnDItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = DnDCore.MODID, value = Dist.CLIENT)
public class TooltipHandler {
    private TooltipHandler() {
        /* This utility class should not be instantiated */
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        DnDCore.LOGGER.info("onItemTooltip: Called");
        ItemStack stack = event.getItemStack();
        Player entity = event.getEntity();
        if (entity == null) {
            DnDCore.LOGGER.info("onItemTooltip: Player is null");
            return;
        }
        Level level = entity.level();

        Dice dice = DnDItemUtils.getDice(stack, level);
        DnDCore.LOGGER.info("onItemTooltip: Data is not null");

        List<Component> tooltips = event.getToolTip();
        int insertIndex = -1;

        for (int i = 0; i < tooltips.size(); i++) {
            Component line = tooltips.get(i);
            if (line.getContents() instanceof TranslatableContents trans &&
                    trans.getKey().startsWith("item.modifiers")) {
                insertIndex = i + 1;
                break;
            }
        }

        if (insertIndex == -1) return;

        Component diceLine = Component.literal(" ")
                .append(dice.toString())
                .append(" ")
                .append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(ChatFormatting.DARK_GREEN);
        tooltips.set(insertIndex, diceLine);

        int attackRollBonus = DnDItemUtils.getAttackRollBonus(stack);
        if (attackRollBonus == 0) return;
        insertIndex++;

        if (tooltips.get(insertIndex + 1).contains(Component.translatable("attribute.name.generic.attack_speed")))
            insertIndex++;

        String attackRoll = attackRollBonus > 0 ?
                "+" + attackRollBonus :
                String.valueOf(attackRollBonus);
        Component bonusLine = Component.literal(" ")
                .append(attackRoll)
                .append(" ")
                .append(Component.translatable("attribute.name.generic.attack_roll_bonus"))
                .withStyle(ChatFormatting.DARK_GREEN);
        tooltips.add(insertIndex, bonusLine);
        DnDCore.LOGGER.info("onItemTooltip: Closed");
    }


}
