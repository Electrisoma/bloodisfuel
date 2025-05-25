
package net.electrisoma.bloodisfuel.content.fluids.blocks;

import net.electrisoma.bloodisfuel.api.fluids.blocks.AbstractFluidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;


public class BloodBlock extends AbstractFluidBlock {
    public BloodBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }
}
