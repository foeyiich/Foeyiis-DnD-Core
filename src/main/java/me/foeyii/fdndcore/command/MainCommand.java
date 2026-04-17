package me.foeyii.fdndcore.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.foeyii.fdndcore.command.subcommand.AbilityScoreSubcommand;
import me.foeyii.fdndcore.command.subcommand.DiceSubcommand;
import me.foeyii.fdndcore.command.subcommand.ItemSubcommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class MainCommand {
    private MainCommand() {
        /* This utility class should not be instantiated */
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        final LiteralCommandNode<CommandSourceStack> fdndNode = dispatcher.register(Commands.literal("fdnd")
                .requires(cs -> cs.hasPermission(2))
                .then(AbilityScoreSubcommand.register())
                .then(DiceSubcommand.register())
                .then(ItemSubcommand.register())
        );
        dispatcher.register(
                Commands.literal("dnd")
                        .redirect(fdndNode)
        );
    }

}
