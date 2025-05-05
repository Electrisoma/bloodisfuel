package net.electrisoma.bloodisfuel.registry.fluids.fluidtypes;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.material.FluidState;

import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;

import org.joml.Vector3f;

import java.util.function.Consumer;


public abstract class BaseFluidType extends FluidType {

    protected static final int NO_TINT = 0xffffffff;
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    //private final ResourceLocation overlayTexture;

    public BaseFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {

        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        //this.overlayTexture = overlayTexture;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {

        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

//            @Override
//            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
//                return overlayTexture;
//            }

            @Override
            public int getTintColor(FluidStack stack) {
                return BaseFluidType.this.getTintColor(stack);
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return BaseFluidType.this.getTintColor(state, getter, pos);
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return BaseFluidType.this.getCustomFogColor();
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {

                float modifier = BaseFluidType.this.getFogDistanceModifier();
                float baseWaterFog = 96.0f;
                if (modifier != 1f) {
                    RenderSystem.setShaderFogShape(FogShape.CYLINDER);
                    RenderSystem.setShaderFogStart(-8);
                    RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
                }
            }
        });
    }

    protected abstract int getTintColor(FluidStack stack);

    protected abstract int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos);

    protected Vector3f getCustomFogColor() {
        return null;
    }

    protected float getFogDistanceModifier() {
        return 1f;
    }
}