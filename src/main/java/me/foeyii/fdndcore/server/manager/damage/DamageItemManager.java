package me.foeyii.fdndcore.server.manager.damage;

import me.foeyii.fdndcore.FoeyiisDnDCore;
import me.foeyii.fdndcore.server.manager.dice.Dice;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class DamageItemManager {
    private static final ResourceLocation ATTRIBUTE_ID =
            ResourceLocation.fromNamespaceAndPath(FoeyiisDnDCore.MODID, "dice_damage");

    public static void setDice(ItemStack stack, String formattedDice) {
        stack.set(DamageItemDataComponent.DICE_DAMAGE, formattedDice);

        syncAttribute(stack, formattedDice);
    }

    public static String getDice(ItemStack stack) {
        return stack.get(DamageItemDataComponent.DICE_DAMAGE);
    }

    private static void syncAttribute(ItemStack stack, String formattedDice) {
        Dice.ParseResult result = Dice.parseFormatted(formattedDice);
        if (!result.success()) return;

        final double baseDamage;
        if (result.count() == 1) {
            baseDamage = result.sides();
        } else {
            baseDamage = result.count() * (result.sides() + 1) / 2.0;
        }

        stack.update(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY, current -> {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            for (var entry : current.modifiers()) {
                if (!entry.modifier().id().equals(ATTRIBUTE_ID)) {
                    builder.add(entry.attribute(), entry.modifier(), entry.slot());
                }
            }
            builder.add(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ATTRIBUTE_ID, baseDamage, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND);
            return builder.build();
        });
    }

}
