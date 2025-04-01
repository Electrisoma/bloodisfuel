package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.config.BConfig;
import net.electrisoma.bloodisfuel.registry.BFluids;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;


public class BClientCommon {

    public static void setFogColor(Camera info, SetColorWrapper wrapper) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        BlockPos blockPos = info.getBlockPosition();
        assert level != null;
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

        Fluid fluid = fluidState.getType();

        if (BFluids.VISCERA.get().isSame(fluid)) {
            wrapper.setFogColor(101 / 255f, 11 / 255f, 15 / 255f);
        }

        if (BFluids.BLOOD.get().isSame(fluid)) {
            wrapper.setFogColor(87 / 255f, 0 / 255f, 0 / 255f);
        }

        if (BFluids.ENRICHED_BLOOD.get().isSame(fluid)) {
            wrapper.setFogColor(131 / 255f, 0 / 255f, 0 / 255f);
        }
    }

    public static float getFogDensity(Camera info, float farDistance) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos))
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

        return -1;
    }

    public interface SetColorWrapper {
        void setFogColor(float r, float g, float b);
    }
}
