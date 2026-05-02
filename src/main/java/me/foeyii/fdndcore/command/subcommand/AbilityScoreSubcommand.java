package me.foeyii.fdndcore.command.subcommand;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import me.foeyii.fdndcore.data.DnDAbilityScoreType;
import me.foeyii.fdndcore.data.DnDRegistries;
import me.foeyii.fdndcore.system.abilityscore.AbilityScore;
import me.foeyii.fdndcore.system.abilityscore.AbilityScoreType;
import me.foeyii.fdndcore.utility.DnDLogger;
import me.foeyii.fdndcore.utility.FText;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AbilityScoreSubcommand {
    private AbilityScoreSubcommand() {
        /* This utility class should not be instantiated */
    }

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandBuildContext mainContext) {
        return Commands.literal("abilityscore")
                .then(Commands.literal("get")
                        .then(Commands.argument("get_target", EntityArgument.entity())
                                .executes(context ->
                                        getAbilityScore(context.getSource(), EntityArgument.getEntity(context, "get_target"))
                                )
                        )
                )
                .then(Commands.literal("set")
                        .then(Commands.argument("set_targets", EntityArgument.entity())
                                .then(Commands.argument("type", ResourceArgument.resource(mainContext, DnDRegistries.ABILITY_SCORE))
                                        .then(Commands.argument("value", IntegerArgumentType.integer(AbilityScore.MIN_VALUE, AbilityScore.MAX_VALUE))
                                                .executes(context -> setAbilityScore(
                                                                context.getSource(),
                                                                EntityArgument.getEntities(context, "set_targets"),
                                                                ResourceArgument.getResource(context, "type", DnDRegistries.ABILITY_SCORE),
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
                                                AbilityScore.DEFAULT_VALUE
                                        )
                                )
                        )
                )
                ;
    }


    private static int getAbilityScore(@NotNull CommandSourceStack source, @NotNull Entity target) {

        if (!isEntityALivingEntity(target))
            return 0;
        AbilityScore targetScores = AbilityScore.get((LivingEntity) target);

        StringBuilder result = new StringBuilder();
        result.append(FText.formatPrefixed("&6" + target.getName().getString() + "'s Stats:\n"));
        for (Holder<AbilityScoreType> type : DnDAbilityScoreType.ABILITY_SCORE_TYPES.getEntries()) {
            result.append(FText.formatPrefixed("  &e" + type.value().displayName() + ": " + targetScores.getScore(type) + '\n'));
        }

        source.sendSystemMessage(Component.literal(result.toString()));

        return 1;
    }

    private static int setAbilityScore(@NotNull CommandSourceStack source, @NotNull Collection<? extends Entity> targets, @NotNull Holder<AbilityScoreType> type, int value) {
        int affectedPlayers = targets.size();
        for (Entity target : targets) {
            if (!isEntityALivingEntity(target))
                return 0;
            AbilityScore targetAbilityScore = AbilityScore.get((LivingEntity) target);
            targetAbilityScore.setScore(type, value);
        }
        String msg = FText.formatPrefixed("&6" + affectedPlayers + " &eentity affected &7(" + type.value().displayName() + " = " + AbilityScore.clamp(value) + ")");
        source.sendSystemMessage(Component.literal(msg));

        return affectedPlayers;
    }

    private static int setAllAbilityScore(@NotNull CommandSourceStack source, @NotNull Collection<? extends Entity> targets, int value) {
        int affectedPlayers = targets.size();
        for (Entity target : targets) {
            if (!isEntityALivingEntity(target))
                return 0;
            AbilityScore targetAbilityScore = AbilityScore.get((LivingEntity) target);
            targetAbilityScore.setAllScore(value);
        }
        String msg = FText.formatPrefixed("&6" + affectedPlayers + " &eentity affected &7(STATS RESET)");
        source.sendSystemMessage(Component.literal(msg));

        return affectedPlayers;
    }

    private static boolean isEntityALivingEntity(Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            DnDLogger.getLogger(AbilityScoreSubcommand.class).error("Target is not a LivingEntity!");
            return false;
        }
        return true;
    }


}
