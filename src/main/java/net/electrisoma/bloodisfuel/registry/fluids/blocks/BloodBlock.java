
package net.electrisoma.bloodisfuel.registry.fluids.blocks;

import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;


public class BloodBlock extends AbstractFluidBlock {
    public BloodBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }
}
