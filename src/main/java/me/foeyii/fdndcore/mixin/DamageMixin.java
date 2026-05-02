package me.foeyii.fdndcore.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommonHooks.class)
public abstract class DamageMixin {
    private DamageMixin() {
        /* This utility class should not be instantiated */
    }

    @Inject(method = "onLivingDamagePre", at = @At("HEAD"))
    private static void onLivingDamagePre(LivingEntity entity, DamageContainer container, CallbackInfoReturnable<Float> cir) {

    }

    @Inject(method = "onLivingDamagePost", at = @At("HEAD"))
    private static void onLivingDamagePost(LivingEntity entity, DamageContainer container, CallbackInfo ci) {

    }

}
