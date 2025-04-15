package net.electrisoma.bloodisfuel.registry.fluid_utils;

import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;


@SuppressWarnings("all")
public class BloodLiquidBlock extends BLiquidBlock {

    public BloodLiquidBlock(NonNullSupplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }
}