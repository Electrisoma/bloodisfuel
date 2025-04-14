package net.electrisoma.bloodisfuel.forge.mixin;

import net.electrisoma.bloodisfuel.registry.fluid_utils.BLiquidBlock;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;


@Mixin(BLiquidBlock.class)
public class BLiquidBlockMixin extends LiquidBlock {

    @Unique
    private List<FluidState> bloodisfuel$stateCache = null;

    @Deprecated
    public BLiquidBlockMixin(FlowingFluid arg, Properties arg2) {
        super(arg, arg2);
    }

    @Nonnull
    @Override
    public FluidState getFluidState(BlockState arg) {
        int i = arg.getValue(LEVEL);
        if (this.bloodisfuel$stateCache == null) {
            this.initFluidStateCache();
        }
        return this.bloodisfuel$stateCache.get(Math.min(i, 8));
    }

    protected synchronized void initFluidStateCache() {
        if (this.bloodisfuel$stateCache == null) {
            this.bloodisfuel$stateCache = new ArrayList<>();
            this.bloodisfuel$stateCache.add(this.getFluid().getSource(false));
            for(int i = 1; i < 8; ++i) {
                this.bloodisfuel$stateCache.add(this.getFluid().getFlowing(8 - i, false));
            }
            this.bloodisfuel$stateCache.add(this.getFluid().getFlowing(8, true));
        }
    }
}