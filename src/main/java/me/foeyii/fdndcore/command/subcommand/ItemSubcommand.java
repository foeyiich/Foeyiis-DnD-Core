package me.foeyii.fdndcore.command.subcommand;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import me.foeyii.fdndcore.data.DnDRegistries;
import me.foeyii.fdndcore.system.damage.DamageType;
import me.foeyii.fdndcore.system.dice.DiceNotation;
import me.foeyii.fdndcore.utility.DnDDamageResolver;
import me.foeyii.fdndcore.utility.DnDItemUtils;
import me.foeyii.fdndcore.utility.FText;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
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

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandBuildContext mainContext) {
        final String damageDiceLiteral = "damage";
        final String damageTypeLiteral = "damage_type";
        final String attackRollBonusLiteral = "attack_roll_bonus";

        final String diceNotationLiteral = "dice_notation";
        final String targetLiteral = "target";

        return Commands.literal("item")
                .then(Commands.literal("set")
                        .then(Commands.literal(damageDiceLiteral)
                                .then(Commands.argument(diceNotationLiteral, StringArgumentType.word())
                                        .executes(context -> Set.itemDamage(
                                                context.getSource(),
                                                StringArgumentType.getString(context, diceNotationLiteral),
                                                null
                                        ))
                                        .then(Commands.argument(targetLiteral, EntityArgument.entity())
                                                .executes(context -> Set.itemDamage(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, diceNotationLiteral),
                                                        EntityArgument.getEntity(context, targetLiteral)
                                                ))
                                        )
                                )
                        )
                        .then(Commands.literal(damageTypeLiteral)
                                .then(Commands.argument("type", ResourceArgument.resource(mainContext, DnDRegistries.DAMAGE_TYPE))
                                        .executes(context -> Set.itemDamageType(
                                                context.getSource(),
                                                ResourceArgument.getResource(context, "type", DnDRegistries.DAMAGE_TYPE),
                                                null
                                        ))
                                        .then(Commands.argument(targetLiteral, EntityArgument.entity())
                                                .executes(context -> Set.itemDamageType(
                                                        context.getSource(),
                                                        ResourceArgument.getResource(context, "type", DnDRegistries.DAMAGE_TYPE),
                                                        EntityArgument.getEntity(context, targetLiteral)
                                                ))
                                        )
                                )
                        )
                        .then(Commands.literal(attackRollBonusLiteral)
                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(context -> Set.itemAttackRollBonus(
                                                context.getSource(),
                                                IntegerArgumentType.getInteger(context, "value"),
                                                null
                                        ))
                                        .then(Commands.argument(targetLiteral, EntityArgument.entity())
                                                .executes(context -> Set.itemAttackRollBonus(
                                                        context.getSource(),
                                                        IntegerArgumentType.getInteger(context, "value"),
                                                        EntityArgument.getEntity(context, targetLiteral)
                                                ))
                                        )
                                )
                        )
                )
                .then(Commands.literal("get")
                        .then(Commands.literal(damageDiceLiteral)
                                .executes(context -> Get.itemDamage(
                                        context.getSource(),
                                        null
                                ))
                                .then(Commands.argument(targetLiteral, EntityArgument.entity())
                                        .executes(context -> Get.itemDamage(
                                                context.getSource(),
                                                EntityArgument.getEntity(context, targetLiteral)
                                        ))
                                )
                        )
                        .then(Commands.literal(damageTypeLiteral)
                                .executes(context -> Get.itemDamageType(
                                        context.getSource(),
                                        null
                                ))
                                .then(Commands.argument(targetLiteral, EntityArgument.entity())
                                        .executes(context -> Get.itemDamageType(
                                                context.getSource(),
                                                EntityArgument.getEntity(context, targetLiteral)
                                        ))
                                )
                        )
                        .then(Commands.literal(attackRollBonusLiteral)
                                .executes(context -> Get.itemAttackRollBonus(
                                        context.getSource(),
                                        null
                                ))
                                .then(Commands.argument(targetLiteral, EntityArgument.entity())
                                        .executes(context -> Get.itemDamageType(
                                                context.getSource(),
                                                EntityArgument.getEntity(context, targetLiteral)
                                        ))
                                )
                        )
                )
                ;
    }

    private static class Set {
        private static int itemDamage(CommandSourceStack source, String damage, @Nullable Entity target) {
            ItemStack itemStack = getMainHandItemStack(source, target);
            if (itemStack == null) return 0;

            DnDItemUtils.setOverrideDiceDamage(itemStack, DiceNotation.parse(damage));
            return 1;
        }

        private static int itemDamageType(CommandSourceStack source, Holder<DamageType> damageType, @Nullable Entity target) {
            ItemStack itemStack = getMainHandItemStack(source, target);
            if (itemStack == null) return 0;

            DnDItemUtils.setOverrideDamageType(itemStack, damageType);
            return 1;
        }


        private static int itemAttackRollBonus(CommandSourceStack source, int attackRollBonus, @Nullable Entity target) {
            ItemStack itemStack = getMainHandItemStack(source, target);
            if (itemStack == null) return 0;

            DnDItemUtils.setOverrideAttackDamageRoll(itemStack, attackRollBonus);
            return 1;
        }
    }

    private static class Get {
        private static int itemDamage(CommandSourceStack source, @Nullable Entity target) {
            ItemStack itemStack = getMainHandItemStack(source, target);
            if (itemStack == null) return 0;

            if (target == null) return 0;
            source.sendSystemMessage(
                    Component.literal(FText.PREFIX)
                            .append(target.getName().getString())
                            .append("'s ")
                            .append(itemStack.getDisplayName())
                            .append(" deals")
                            .append(DnDDamageResolver.resolveEffectiveDiceDamage(itemStack).toString())
                            .append(" Damage")
                            .withStyle(ChatFormatting.YELLOW)
            );
            return 1;
        }

        private static int itemDamageType(CommandSourceStack source, @Nullable Entity target) {
            ItemStack itemStack = getMainHandItemStack(source, target);
            if (itemStack == null) return 0;

            if (target == null) return 0;
            source.sendSystemMessage(
                    Component.literal(FText.PREFIX)
                            .append(target.getName().getString())
                            .append("'s ")
                            .append(itemStack.getDisplayName())
                            .append(" deals")
                            .append(DnDDamageResolver.resolveEffectiveDamageType(itemStack).toString())
                            .append(" Damage Type")
                            .withStyle(ChatFormatting.YELLOW)
            );
            return 1;
        }

        private static int itemAttackRollBonus(CommandSourceStack source, @Nullable Entity target) {
            ItemStack itemStack = getMainHandItemStack(source, target);
            if (itemStack == null) return 0;

            if (target == null) return 0;

            int attackRollBonus = DnDDamageResolver.resolveAttackRollBonus(itemStack);
            String attackRollBonusDisplay = attackRollBonus > 0 ? "+" + attackRollBonus : String.valueOf(attackRollBonus);
            source.sendSystemMessage(
                    Component.literal(FText.PREFIX)
                            .append(target.getName().getString())
                            .append("'s ")
                            .append(itemStack.getDisplayName())
                            .append(" gives")
                            .append(attackRollBonusDisplay)
                            .append(" Attack Roll Bonus")
                            .withStyle(ChatFormatting.YELLOW)
            );
            return 1;
        }
    }

    private static @Nullable ItemStack getMainHandItemStack(CommandSourceStack source, @Nullable Entity target) {
        if (target == null) {
            if (!isExecutorAPlayer(source)) {
                source.sendSystemMessage(Component.literal("Only players can execute this command!"));
                return null;
            }
            target = source.getPlayer();
        }

        if (!(target instanceof LivingEntity livingEntity)) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cTarget is not a living entity!")));
            return null;
        }

        ItemStack itemStack = livingEntity.getMainHandItem();
        if (itemStack.is(Items.AIR)) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cNo Item Is Being Hold!")));
            return null;
        }
        return itemStack;
    }

    private static boolean isExecutorAPlayer(@NotNull CommandSourceStack source) {
        if (!source.isPlayer()) {
            source.sendSystemMessage(Component.literal(FText.formatPrefixed("&cOnly Player Can Run This!")));
            return false;
        }
        return true;
    }

}
