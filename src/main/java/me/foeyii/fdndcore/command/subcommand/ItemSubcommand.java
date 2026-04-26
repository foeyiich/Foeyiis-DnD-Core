package me.foeyii.fdndcore.command.subcommand;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import me.foeyii.fdndcore.system.dice.Dice;
import me.foeyii.fdndcore.system.dice.DiceNotation;
import me.foeyii.fdndcore.system.dice.exception.InvalidDiceNotationException;
import me.foeyii.fdndcore.utility.DnDItemUtils;
import me.foeyii.fdndcore.utility.FText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemSubcommand {
    private ItemSubcommand() {
        /* This utility class should not be instantiated */
    }

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("item")
                .then(Commands.literal("dice_damage")
                        .then(Commands.literal("set")
                                .then(Commands.argument("dice_notation", StringArgumentType.word())
                                        .executes(context ->
                                                setItemDiceDamage(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "dice_notation"),
                                                        null
                                                )
                                        )
                                )
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(context ->
                                                setItemDiceDamage(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "dice_notation"),
                                                        EntityArgument.getEntity(context, "target")
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("get")
                                .executes(context -> getItemDiceDamage(context.getSource(), null))
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(context -> getItemDiceDamage(
                                                        context.getSource(),
                                                        EntityArgument.getEntity(context, "target")
                                                )
                                        )
                                )
                        )
                )
                ;
    }

    private static int setItemDiceDamage(CommandSourceStack source, String damage, @Nullable Entity target) {

        if (target == null) {
            if (!isExecutorAPlayer(source)) return 0;
            target = source.getPlayer();
        }

        if (!(target instanceof LivingEntity livingEntity)) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cTarget is not a living entity!")));
            return 0;
        }

        Dice dice;
        try {
            dice = DiceNotation.parse(damage);
        } catch (InvalidDiceNotationException e) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cInvalid Dice Notation!")));
            return 0;
        }

        ItemStack itemStack = livingEntity.getMainHandItem();

        if (itemStack.is(Items.AIR)) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cNo Item Is Being Hold!")));
            return 0;
        }

        DnDItemUtils.setCustomDiceDamage(itemStack, dice);
        return 1;
    }

    private static int getItemDiceDamage(CommandSourceStack source, @Nullable Entity target) {
        String targetLabel = "Executor";
        if (target == null) {
            if (!source.isPlayer()) {
                source.sendSystemMessage(Component.literal(FText.formatPrefixed("&c" + targetLabel + " is not a player!")));
                return 0;
            }
            target = source.getPlayer();
        } else {
            targetLabel = target.getName().toString();
        }
        if (!(target instanceof LivingEntity livingEntityTarget)) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&c" + targetLabel + " is not a Living Entity!")));
            return 0;
        }
        ItemStack mainHandItem = livingEntityTarget.getMainHandItem();

        if (mainHandItem.isEmpty()) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&c" + targetLabel + "'s main hand item is empty!")));
            return 0;
        }

        source.sendSystemMessage(Component.literal(FText.formatPrefixed("&eItem Dice Damage: " + DnDItemUtils.getDice(mainHandItem, target.level()))));

        return 1;
    }


    private static boolean isExecutorAPlayer(@NotNull CommandSourceStack source) {
        if (!source.isPlayer()) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cOnly Player Can Run This!")));
            return false;
        }
        return true;
    }

}
