package me.foeyii.fdndcore.server.manager.abilityscore;

import me.foeyii.fdndcore.FoeyiisDnDCore;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static me.foeyii.fdndcore.FoeyiisDnDCore.ATTACHMENT_TYPES;

public class AbilityScoreManager {

    public record AttributeBonus(Holder<Attribute> attribute, String modifierId, double value) {
    }

    public static void register(IEventBus bus) {
        NeoForge.EVENT_BUS.register(new AbilityScoreEventHandler());
    }

    public static final Supplier<AttachmentType<AbilityScoreContainer>> ATTACHMENT_TYPE =
            ATTACHMENT_TYPES.register("stats", () -> AttachmentType.builder(AbilityScoreContainer::new)
                    .serialize(new AbilityScoreSerializer())
                    .copyOnDeath()
                    .build());

    public static class AttributeModifierRate {
        private AttributeModifierRate() {
            /* This utility class should not be instantiated */
        }

        public static final float STRENGTH_ATTACK_DAMAGE = 0.3f;
        public static final float CONSTITUION_MAX_HEALTH = 4.f;
        public static final float DEXTERITY_MOVEMENT_SPEED = 0.04f;
        public static final float DEXTERITY_SNEAKING_SPEED = 0.02f;
        public static final float WISDOM_FOLLOW_RANGE = 4.f;
    }

    public static void syncAttribute(LivingEntity entity, AbilityScoreContainer.Types type) {
        getBonuses(entity, type).forEach(bonus -> {
            applyModifier(entity, bonus.attribute(), bonus.modifierId(), bonus.value);
        });
    }

    public static void syncAttributes(LivingEntity entity) {
        for (AbilityScoreContainer.Types type : AbilityScoreContainer.Types.values()) {
            syncAttribute(entity, type);
        }
    }

    private static void applyModifier(LivingEntity entity, Holder<Attribute> attr, String name, double value) {
        AttributeInstance inst = entity.getAttribute(attr);
        if (inst != null) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(FoeyiisDnDCore.MODID, name);
            inst.removeModifier(id);
            inst.addPermanentModifier(new AttributeModifier(id, value, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    public static List<AttributeBonus> getBonuses(LivingEntity entity, AbilityScoreContainer.Types type) {
        AbilityScoreContainer stats = entity.getData(ATTACHMENT_TYPE);
        int mod = stats.getScoreModifier(type);
        List<AttributeBonus> bonuses = new ArrayList<>();

        switch (type) {
            case STRENGTH ->
                    bonuses.add(new AttributeBonus(Attributes.ATTACK_DAMAGE, "str_attack_damage", mod * AttributeModifierRate.STRENGTH_ATTACK_DAMAGE));

            case CONSTITUTION ->
                    bonuses.add(new AttributeBonus(Attributes.MAX_HEALTH, "con_health", mod * AttributeModifierRate.CONSTITUION_MAX_HEALTH));

            case DEXTERITY -> {
                bonuses.add(new AttributeBonus(Attributes.MOVEMENT_SPEED, "dex_move_speed", mod * AttributeModifierRate.DEXTERITY_MOVEMENT_SPEED));
                bonuses.add(new AttributeBonus(Attributes.SNEAKING_SPEED, "dex_sneak_speed", mod * AttributeModifierRate.DEXTERITY_SNEAKING_SPEED));
            }
            case WISDOM ->
                    bonuses.add(new AttributeBonus(Attributes.FOLLOW_RANGE, "wis_range", mod * AttributeModifierRate.WISDOM_FOLLOW_RANGE));

        }
        return bonuses;
    }

}
