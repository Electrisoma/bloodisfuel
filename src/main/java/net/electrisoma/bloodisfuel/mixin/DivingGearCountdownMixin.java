package net.electrisoma.bloodisfuel.mixin;

import net.electrisoma.bloodisfuel.registry.BTags;

import com.simibubi.create.content.equipment.armor.RemainingAirOverlay;

import net.minecraft.tags.TagKey;
import net.minecraft.core.BlockPos;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;


/**
 * Useless in the next update
 */
@Mixin(RemainingAirOverlay.class)
public class DivingGearCountdownMixin {

    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z")
    )

    private boolean modifyEyeInFluidCheck(LocalPlayer player, TagKey<Fluid> tagKey, Operation<Boolean> original) {

        BlockPos eyePos = BlockPos.containing(player.getEyePosition(0f));
        FluidState fluid = player.level().getFluidState(eyePos);

        if (original.call(player, tagKey)) return true;

        return fluid.is(BTags.BFluidTags.DIVING_FLUID.tag);
    }
}