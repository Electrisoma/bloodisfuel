package net.electrisoma.bloodisfuel.registry.fluids.fluids;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraftforge.fluids.ForgeFlowingFluid;


public class BloodFluid extends ForgeFlowingFluid {

    protected BloodFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState fluidState) {
        return false;
    }

    @Override
    public int getAmount(FluidState fluidState) {
        return 8;
    }

    public static class Flowing extends BloodFluid {
        public Flowing(Properties properties) {
            super(properties);
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> fluidState) {
            super.createFluidStateDefinition(fluidState);
            fluidState.add(LEVEL);
        }

        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

    }

    public static class Source extends BloodFluid {
        public Source(Properties properties) {
            super(properties);
        }

        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}
