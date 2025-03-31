package net.electrisoma.bloodisfuel;

import net.electrisoma.bloodisfuel.registry.BFluids;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.Arrays;
import java.util.List;


public class BClientCommon {

    public static void setFogColor(Camera info, SetColorWrapper wrapper) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

        Fluid fluid = fluidState.getType();

        if (BFluids.VISCERA.get().isSame(fluid)) {
            wrapper.setFogColor(101 / 255f, 11 / 255f, 15 / 255f);
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
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return -1;

        Fluid fluid = fluidState.getType();

        if (BFluids.VISCERA.get()
                .isSame(fluid)) {
            //fogData.scaleFarPlaneDistance(1f / 32f * BConfigs.client().visceraTransparencyMultiplier.getF());
            //fogData.scaleFarPlaneDistance(1f / 32f * 1);
            return -1;
        }

        return -1;
    }
}
