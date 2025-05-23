package net.electrisoma.bloodisfuel.registry.fluids.fluidtypes;

import net.createmod.catnip.theme.Color;

import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;

import net.minecraftforge.fluids.FluidStack;

import org.joml.Vector3f;

import java.util.function.Supplier;


public class BoilingBloodFluidType extends AbstractFluidType {
    private Vector3f fogColor;
    private Supplier<Float> fogDistance;

    public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance, String overlayType) {
        return (p, s, f) -> {
            BoilingBloodFluidType fluidType = new BoilingBloodFluidType(p, s, f, overlayType);
            fluidType.fogColor = new Color(fogColor, false).asVectorF();
            fluidType.fogDistance = fogDistance;
            return fluidType;
        };
    }

    private BoilingBloodFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, String overlayType) {
        super(properties, stillTexture, flowingTexture, overlayType);
    }

    @Override protected int getTintColor(FluidStack stack) {
        return NO_TINT;
    }
    @Override public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
        return 0x00ffffff;
    }
    @Override protected Vector3f getCustomFogColor() {
        return fogColor;
    }
    @Override protected float getFogDistanceModifier() {
        return fogDistance.get();
    }
}