package me.foeyii.fdndcore.client;

import me.foeyii.fdndcore.server.manager.damage.DamageItemManager;
import me.foeyii.fdndcore.server.manager.dice.Dice;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class ClientItemTooltipHandler {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        List<Component> tooltip = event.getToolTip();
        if (tooltip.isEmpty()) return;

        ItemStack itemStack = event.getItemStack();

        Entity entity = event.getEntity();
        if (entity == null) return;

        Dice dice = DamageItemManager.getDiceFinalDamage(itemStack, entity.level());
        if (dice.getCount() <= 0) return;

        String attackDamageLabel = Component.translatable("attribute.name.generic.attack_damage").getString();

        for (int i = 0; i < tooltip.size(); i++) {
            String lineText = tooltip.get(i).getString();
            if (lineText.contains(attackDamageLabel)) {
                Component komponenBaru = Component.literal(" ")
                        .append(Component.literal(dice.toString())).append(" ")
                        .append(Component.translatable("attribute.name.generic.attack_damage"))
                        .withStyle(ChatFormatting.DARK_GREEN);
                tooltip.set(i, komponenBaru);
                break;
            }
        }

    }


}
