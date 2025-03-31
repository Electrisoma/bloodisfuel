package net.electrisoma.bloodisfuel.forge.mixin;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;

@Mixin(BFlowingFluid.class)
public abstract class BFlowingFluidMixin extends FlowingFluid {

    @Shadow
    @Final
    protected ResourceLocation stillTex;
    @Shadow
    @Final
    protected ResourceLocation flowingTex;
    @Shadow
    @Final
    protected int color;
    @Shadow
    @Final
    protected SoundEvent fillSound;
    @Shadow
    @Final
    protected SoundEvent emptySound;

    @Unique
    private FluidType fluidType;

    @Nonnull
    @Override
    public FluidType getFluidType() {
        if (this.fluidType == null) {
            this.fluidType = CreateRegistrate.defaultFluidType(
                    FluidType.Properties.create()
                            .sound(SoundActions.BUCKET_FILL, this.fillSound)
                            .sound(SoundActions.BUCKET_EMPTY, this.emptySound)
                            .descriptionId(Util.makeDescriptionId("fluid", BuiltInRegistries.FLUID.getKey(this))), this.stillTex, this.flowingTex);
        }
        return this.fluidType;
    }
}