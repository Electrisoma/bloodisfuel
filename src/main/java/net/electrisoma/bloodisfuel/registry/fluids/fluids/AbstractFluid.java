package net.electrisoma.bloodisfuel.registry.fluids.fluids;

import net.electrisoma.bloodisfuel.registry.BParticles;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.StateDefinition;

import net.minecraftforge.fluids.ForgeFlowingFluid;


@SuppressWarnings("unused")
public abstract class AbstractFluid extends ForgeFlowingFluid {
    protected AbstractFluid(Properties properties) {
        super(properties);
    }

    @Override public boolean isSource(FluidState fluidState) {
        return false;
    }
    @Override public int getAmount(FluidState fluidState) {
        return 8;
    }
    @Override public ParticleOptions getDripParticle() {
        return BParticles.BLOOD_DROP.orElse(null);
    }

    public static class Flowing extends AbstractFluid {
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
    public static class Source extends AbstractFluid {
        public Source(Properties properties) {
            super(properties);
        }
        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}