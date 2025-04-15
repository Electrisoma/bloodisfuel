package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.config.BConfig;
import net.electrisoma.bloodisfuel.registry.BFluids;

import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;


@SuppressWarnings({"all"})
public class BClientCommon {

    public static Fluid getCamera(Camera camera) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        BlockPos blockPos = camera.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);

        if (camera.getPosition().y >= blockPos.getY() + fluidState.getHeight(level, blockPos)) {
            return null;
        }

        return fluidState.getType();
    }

    public static void setFogColor(Camera camera, SetColorWrapper wrapper) {
        Fluid fluid = getCamera(camera);
        if (!isCustomFluid(fluid)) return;

        if (BFluids.VISCERA.get().isSame(fluid)) {
            wrapper.setFogColor(101 / 255f, 11 / 255f, 15 / 255f);
            return;
        }

        if (BFluids.BLOOD.get().isSame(fluid)) {
            wrapper.setFogColor(87 / 255f, 0 / 255f, 0 / 255f);
            return;
        }

        if (BFluids.ENRICHED_BLOOD.get().isSame(fluid)) {
            wrapper.setFogColor(131 / 255f, 0 / 255f, 0 / 255f);
            return;
        }

        if (BFluids.OIL_ENRICHED_BLOOD.get().isSame(fluid)) {
            wrapper.setFogColor(131 / 255f, 0 / 255f, 0 / 255f);
            return;
        }

        if (BFluids.GASOLINE_INFUSED_BLOOD.get().isSame(fluid)) {
            wrapper.setFogColor(131 / 255f, 0 / 255f, 0 / 255f);
            return;
        }

        if (BFluids.DIESEL_INFUSED_BLOOD.get().isSame(fluid)) {
            wrapper.setFogColor(131 / 255f, 0 / 255f, 0 / 255f);
            return;
        }
    }

    public interface SetColorWrapper {
        void setFogColor(float r, float g, float b);
    }

    private static float previousDensity = -1f;

    public static float getFogDensity(Camera camera, float farPlane) {
        Fluid fluid = getCamera(camera);
        if (fluid == null || !isCustomFluid(fluid)) {
            previousDensity = -1;
            return -1;
        }

        float targetDensity = getFluidDensity(fluid);
        float smoothDensity = smoothFog(previousDensity, targetDensity, 0.15f);

        previousDensity = smoothDensity;
        return smoothDensity;
    }

    private static float getFluidDensity(Fluid fluid) {

        if (BFluids.VISCERA.get().isSame(fluid)) {
            return 1 / 32f * BConfig.client().visceraTransparencyMultiplier.getF();
        }

        if (BFluids.BLOOD.get().isSame(fluid)) {
            return 1 / 16f * BConfig.client().bloodTransparencyMultiplier.getF();
        }

        if (BFluids.ENRICHED_BLOOD.get().isSame(fluid)) {
            return 1 / 8f * BConfig.client().enrichedBloodTransparencyMultiplier.getF();
        }

        if (BFluids.OIL_ENRICHED_BLOOD.get().isSame(fluid)) {
            return 1 / 8f * BConfig.client().oilEnrichedBloodTransparencyMultiplier.getF();
        }

        if (BFluids.GASOLINE_INFUSED_BLOOD.get().isSame(fluid)) {
            return 1 / 8f * BConfig.client().gasolineInfusedBloodTransparencyMultiplier.getF();
        }

        if (BFluids.DIESEL_INFUSED_BLOOD.get().isSame(fluid)) {
            return 1 / 8f * BConfig.client().dieselInfusedBloodTransparencyMultiplier.getF();
        }

        return -1;
    }

    private static float smoothFog(float from, float to, float alpha) {
        if (from < 0) return to;
        return from + (to - from) * alpha;
    }

    public static boolean isCustomFluid(Fluid fluid) {
        return fluid != null
                && (BFluids.BLOOD.get().isSame(fluid)
                || BFluids.ENRICHED_BLOOD.get().isSame(fluid)
                || BFluids.VISCERA.get().isSame(fluid)
                || BFluids.OIL_ENRICHED_BLOOD.get().isSame(fluid)
                || BFluids.GASOLINE_INFUSED_BLOOD.get().isSame(fluid)
                || BFluids.DIESEL_INFUSED_BLOOD.get().isSame(fluid)
        );
    }
}
