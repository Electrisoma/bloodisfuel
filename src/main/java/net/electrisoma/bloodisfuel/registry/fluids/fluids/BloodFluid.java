package net.electrisoma.bloodisfuel.registry.fluids.fluids;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.StateDefinition;


@SuppressWarnings("unused")
public class BloodFluid extends AbstractFluid {

    public BloodFluid(Properties properties) {
        super(properties);
    }

    public static class Flowing extends BloodFluid {

        public Flowing(Properties properties) {
            super(properties);
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> FluidState) {

            super.createFluidStateDefinition(FluidState);
            FluidState.add(LEVEL);
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