package net.electrisoma.bloodisfuel.api.utils;


import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.electrisoma.bloodisfuel.api.engine.EngineFluidManager;
import net.minecraft.core.RegistryAccess;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Objects;


@SuppressWarnings("unused")
public interface EngineUtils extends TooltipUtils {
    SmartFluidTankBehaviour getTank();
    int getTick();
    SmartBlockEntity self();

    default RegistryAccess getRegistryAccess() {
        return self().getLevel() != null ? Objects.requireNonNull(self().getLevel()).registryAccess() : null;
    }
    default FluidStack getFuelStack() {
        return getTank().getPrimaryHandler().getFluid();
    }

    default boolean enabled() {
        FluidStack fs = getFuelStack();
        if (fs.isEmpty()) return false;
        RegistryAccess access = getRegistryAccess();
        if (access == null) return false;
        return EngineFluidManager.fromFluid(fs, access) != EngineFluidManager.EMPTY;
    }
    default float getFuelSpeed() {
        FluidStack fs = getFuelStack();
        RegistryAccess access = getRegistryAccess();
        if (access == null || fs.isEmpty()) return 0f;
        return EngineFluidManager.getSpeed(fs, access);
    }
    default float getFuelStress() {
        FluidStack fs = getFuelStack();
        RegistryAccess access = getRegistryAccess();
        if (access == null || fs.isEmpty()) return 0f;
        float speed = getFuelSpeed();
        if (speed == 0f) return 0f;
        return EngineFluidManager.getStrength(fs, access) / speed;
    }
    default void tickFuelUsage(float multiplier) {
        if (getTick() % 20 == 0) {
            FluidStack fs = getFuelStack();
            RegistryAccess access = getRegistryAccess();
            if (access == null || fs.isEmpty()) return;
            float burnRate = EngineFluidManager.getBurnRate(fs, access);
            getTank().getPrimaryHandler().drain((int)(burnRate * multiplier), IFluidHandler.FluidAction.EXECUTE);
        }
    }
    default void tickFuelUsage() {
        tickFuelUsage(1);
    }

    void playSound();
}
