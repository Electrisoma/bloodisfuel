package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.config.BConfig;
import net.electrisoma.bloodisfuel.registry.BFluids;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;


@SuppressWarnings({"all"})
public class BClientCommon {

    public static void setFogColor(Camera info, SetColorWrapper wrapper) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos))
            return;

        Fluid fluid = fluidState.getType();

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

    public static float getFogDensity(Camera info, float farDistance) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y >= blockPos.getY() + fluidState.getHeight(level, blockPos))
            return -1;

        Fluid fluid = fluidState.getType();

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
}
