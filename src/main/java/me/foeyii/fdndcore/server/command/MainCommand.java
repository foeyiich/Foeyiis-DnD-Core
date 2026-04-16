package me.foeyii.fdndcore.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.foeyii.fdndcore.FoeyiisDnDCore;
import me.foeyii.fdndcore.server.manager.abilityscore.AbilityScore;
import me.foeyii.fdndcore.server.manager.abilityscore.AbilityScoreContainer;
import me.foeyii.fdndcore.server.manager.damage.DamageItemDataComponent;
import me.foeyii.fdndcore.server.manager.dice.Dice;
import me.foeyii.fdndcore.utility.FText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.server.command.EnumArgument;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class MainCommand {
    private MainCommand() {
        /* This utility class should not be instantiated */
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        final LiteralCommandNode<CommandSourceStack> fdndNode = dispatcher.register(Commands.literal("fdnd")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("stats")
                        .then(Commands.literal("get")
                                .then(Commands.argument("get_target", EntityArgument.entity())
                                        .executes(context ->
                                                getAbilityScore(context.getSource(), EntityArgument.getEntity(context, "get_target"))
                                        )
                                )
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("set_targets", EntityArgument.entity())
                                        .then(Commands.argument("type", EnumArgument.enumArgument(AbilityScoreContainer.Types.class))
                                                .then(Commands.argument("value", IntegerArgumentType.integer(AbilityScoreContainer.MIN_VALUE, AbilityScoreContainer.MAX_VALUE))
                                                        .executes(context ->
                                                                setAbilityScore(
                                                                        context.getSource(),
                                                                        EntityArgument.getEntities(context, "set_targets"),
                                                                        context.getArgument("type", AbilityScoreContainer.Types.class),
                                                                        IntegerArgumentType.getInteger(context, "value")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("reset")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .executes(context ->
                                                setAllAbilityScore(
                                                        context.getSource(),
                                                        EntityArgument.getEntities(context, "targets"),
                                                        AbilityScoreContainer.DEFAULT_VALUE
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("roll")
                        .executes(context -> rollDice(context.getSource(), "1d6", 0))
                        .then(Commands.argument("formattedRoll", StringArgumentType.word())
                                .executes(context -> rollDice(context.getSource(), StringArgumentType.getString(context, "formattedRoll"), 0))
                                .then(Commands.argument("modifier", IntegerArgumentType.integer())
                                        .executes(context -> rollDice(context.getSource(), StringArgumentType.getString(context, "formattedRoll"), IntegerArgumentType.getInteger(context, "modifier")))
                                )
                        )
                )
                .then(Commands.literal("item")
                        .then(Commands.literal("set_dice_damage")
                                .then(Commands.argument("damage", StringArgumentType.word())
                                        .executes(context -> setItemDiceDamage(context.getSource(), StringArgumentType.getString(context, "damage")))
                                )
                        )
                )
        );
        dispatcher.register(
                Commands.literal("dnd")
                        .redirect(fdndNode)
        );
    }

    private static int getAbilityScore(@NotNull CommandSourceStack source, @NotNull Entity target) {

        if (!isEntityALivingEntity(target))
            return 0;
        AbilityScoreContainer targetScores = AbilityScoreContainer.get((LivingEntity) target);

        String msg = '\n' +
                FText.formatPrefixed("&6" + target.getName().getString() + "'s Stats:\n") +
                FText.formatPrefixed("  &eStrength: " + targetScores.getScore(AbilityScoreContainer.Types.STRENGTH) + '\n') +
                FText.formatPrefixed("  &eDexterity: " + targetScores.getScore(AbilityScoreContainer.Types.DEXTERITY) + '\n') +
                FText.formatPrefixed("  &eConstitution: " + targetScores.getScore(AbilityScoreContainer.Types.CONSTITUTION) + '\n') +
                FText.formatPrefixed("  &eIntelligence: " + targetScores.getScore(AbilityScoreContainer.Types.INTELLIGENCE) + '\n') +
                FText.formatPrefixed("  &eWisdom: " + targetScores.getScore(AbilityScoreContainer.Types.WISDOM) + '\n') +
                FText.formatPrefixed("  &eCharisma: " + targetScores.getScore(AbilityScoreContainer.Types.CHARISMA)) +
                '\n';

        source.sendSystemMessage(Component.literal(msg));

        return 1;
    }

    private static int setAbilityScore(@NotNull CommandSourceStack source, @NotNull Collection<? extends Entity> targets, @NotNull AbilityScoreContainer.Types type, int value) {
        int affectedPlayers = targets.size();
        for (Entity target : targets) {
            if (!isEntityALivingEntity(target))
                return 0;
            AbilityScore targetAbilityScore = new AbilityScore((LivingEntity) target);
            targetAbilityScore.setScore(type, value);
        }
        String msg = FText.formatPrefixed("&6" + affectedPlayers + " &eentity affected &7(" + type + " = " + AbilityScoreContainer.clamp(value) + ")");
        source.sendSystemMessage(Component.literal(msg));

        return affectedPlayers;
    }

    private static int setAllAbilityScore(@NotNull CommandSourceStack source, @NotNull Collection<? extends Entity> targets, int value) {
        int affectedPlayers = targets.size();
        for (Entity target : targets) {
            if (!isEntityALivingEntity(target))
                return 0;
            AbilityScore targetAbilityScore = new AbilityScore((LivingEntity) target);
            targetAbilityScore.setAllScore(value);
        }
        String msg = FText.formatPrefixed("&6" + affectedPlayers + " &eentity affected &7(STATS RESET)");
        source.sendSystemMessage(Component.literal(msg));

        return affectedPlayers;
    }

    private static int rollDice(CommandSourceStack source, String formattedRoll, int modifier) {

        Dice.ParseResult parseResult = Dice.parseFormatted(formattedRoll);
        if (!parseResult.success()) {
            String msg = '\n' +
                    FText.formatPrefixed("&cInvalid Dice Formatted Roll!") + '\n' +
                    FText.formatPrefixed("&cFormat: &l<Roll_Count>d<Dice_Sides>") + '\n' +
                    FText.formatPrefixed("&cEx: &e1d6 &7| &e2d8 &7| &e3d4");
            source.sendSystemMessage(Component.literal(msg));
            return 0;
        }

        Dice dice = Dice.of(parseResult.count(), parseResult.sides());
        int roll = dice.roll();

        String modifierStylized = String.valueOf(modifier);
        if (modifier > 0) {
            modifierStylized = "&a+" + modifier;
        } else if (modifier < 0) {
            modifierStylized = "&c" + modifier;
        }

        String msg = '\n' +
                FText.formatPrefixed("&eRolling &6" + parseResult.count() + "&e Dice(s) with &6" + parseResult.sides() + "&e Sides") + '\n' +
                FText.formatPrefixed("  &eRoll: &f" + roll) + '\n' +
                FText.formatPrefixed("  &eModifier: &f" + modifierStylized) + '\n' +
                FText.formatPrefixed("  &eFinal: &f" + (roll + modifier))
                + '\n';

        source.sendSystemMessage(Component.literal(msg));
        return 1;
    }

    private static int setItemDiceDamage(CommandSourceStack source, String damage) {
        Dice.ParseResult parseResult = Dice.parseFormatted(damage);
        if (!source.isPlayer()) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cOnly Player Can Run This!")));
            return 0;
        }
        if (!parseResult.success()) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cInvalid Dice Format!")));
            return 0;
        }
        Player player = source.getPlayer();
        if (player == null)
            return 0;
        ItemStack itemStack = player.getMainHandItem();

        if (itemStack.is(Items.AIR)) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cNo Item Is Being Hold!")));
        }

        Damage.(DamageItemDataComponent.DICE_DAMAGE, damage);
        return 1;
    }

    private static boolean isEntityALivingEntity(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            FoeyiisDnDCore.getLOGGER().error("Target is not a LivingEntity!");
            return false;
        }
        return true;
    }

}
