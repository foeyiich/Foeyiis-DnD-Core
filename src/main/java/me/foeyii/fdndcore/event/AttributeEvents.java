package me.foeyii.fdndcore.event;

import me.foeyii.fdndcore.DnDCore;
import me.foeyii.fdndcore.data.DnDAttributes;
import me.foeyii.fdndcore.data.DnDDataMaps;
import me.foeyii.fdndcore.data.map.CombatItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

@EventBusSubscriber(modid = DnDCore.MODID)
public class AttributeEvents {
    private AttributeEvents() {
        /* This utility class should not be instantiated */
    }

    @SubscribeEvent
    public static void onAttributeModifiers(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        CombatItem data = stack.getItemHolder().getData(DnDDataMaps.COMBAT_ITEMS);

        if (data != null && data.attackRollBonus() != 0) {
            event.addModifier(
                    DnDAttributes.ATTACK_ROLL_BONUS,
                    new AttributeModifier(
                            ResourceLocation.fromNamespaceAndPath(DnDCore.MODID, "weapon_bonus"),
                            data.attackRollBonus(),
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
            );
        }
    }

}
