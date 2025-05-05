package net.electrisoma.bloodisfuel.registry.fluids.fluids;

import net.electrisoma.bloodisfuel.registry.BParticles;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.StateDefinition;


public abstract class BoilingBloodFluid extends BloodFluid  {

    public BoilingBloodFluid(Properties properties) {
        super(properties);
    }

    @Override
    public ParticleOptions getDripParticle() {
        return BParticles.BOILING_BLOOD_DROP.orElse(null);
    }

    public static class Flowing extends BoilingBloodFluid {
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

    public static class Source extends BoilingBloodFluid {
        public Source(Properties properties) {
            super(properties);
        }

        public boolean isSource(FluidState p_76267_) {
            return true;
        }
    }
}