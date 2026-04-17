package me.foeyii.fdndcore.command.subcommand;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import me.foeyii.fdndcore.manager.dice.Dice;
import me.foeyii.fdndcore.manager.dice.DiceNotation;
import me.foeyii.fdndcore.manager.dice.IllegalDiceSidesException;
import me.foeyii.fdndcore.manager.dice.InvalidDiceNotationException;
import me.foeyii.fdndcore.utility.FText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class DiceSubcommand {
    private DiceSubcommand() {
        /* This utility class should not be instantiated */
    }

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("dice")
                .then(Commands.literal("roll")
                        .then(Commands.argument("formattedDiceRoll", StringArgumentType.string())
                                .executes(context ->
                                        rollDice(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "formattedDiceRoll")
                                        )
                                )
                        )
                )
                .then(Commands.literal("generate_notation")
                        .then(Commands.argument("baseValue", IntegerArgumentType.integer(0))
                                .executes(context -> generateDiceNotation(context.getSource(), IntegerArgumentType.getInteger(context, "baseValue")))
                        )
                )
                ;
    }

    private static int rollDice(CommandSourceStack source, String diceNotation) {

        Dice dice;
        String msg;
        try {
            dice = DiceNotation.parse(diceNotation);
        } catch (InvalidDiceNotationException e) {
            msg = '\n' +
                    FText.formatPrefixed("&cInvalid Dice Notation!") + '\n' +
                    FText.formatPrefixed("&cNOTE: <> as &lREQUIRED &7| &c[] as &6&lOPTIONAL ") + '\n' +
                    FText.formatPrefixed("&cFormat: &e" + DiceNotation.TEMPLATE + "[<+->Flat Modifier]") + '\n' +
                    FText.formatPrefixed("&cEx: &e" + DiceNotation.stringify(1, 6) + " &7| &e" + DiceNotation.stringify(2, 4, 3));
            source.sendSystemMessage(Component.literal(msg));
            return 0;
        } catch (IllegalDiceSidesException e) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cDice sides must be even!")));
            return 0;
        }

        int roll = dice.getPureDice().roll();

        int modifier = dice.getModifier();
        String modifierStylized = "";
        if (modifier > 0) modifierStylized = "&a+" + modifier;
        else if (modifier < 0) modifierStylized = "&c" + modifier;

        msg = '\n' +
                FText.formatPrefixed("&eRolling &6" + dice.getCount() + "&e Dice(s) with &6" + dice.getSides() + "&e Sides") + '\n' +
                FText.formatPrefixed("  &eRoll: &f" + roll) + '\n' +
                FText.formatPrefixed("  &eModifier: &f" + modifierStylized) + '\n' +
                FText.formatPrefixed("  &eFinal: &f" + (roll + modifier))
                + '\n';

        source.sendSystemMessage(Component.literal(msg));
        return 1;
    }

    private static int generateDiceNotation(CommandSourceStack source, int base) {
        String msg = FText.formatPrefixed("&eGenerated Dice: &6" + base + " &e-> &6" + Dice.generateFromInt(base));
        source.sendSystemMessage(Component.literal(msg));
        return 1;
    }

}
