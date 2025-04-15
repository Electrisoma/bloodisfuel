package net.electrisoma.bloodisfuel.registry.fluid_utils;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;


public class HotLiquidBlock extends BLiquidBlock{

    public HotLiquidBlock(NonNullSupplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide) {
            if (!entity.fireImmune()) {
                entity.setSecondsOnFire(5);
            }
            entity.hurt(level.damageSources().lava(), 4.0F);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) == 0) {
            double x = (double) pos.getX() + 0.5;
            double y = (double) pos.getY() + 1.0;
            double z = (double) pos.getZ() + 0.5;

            // Acid bubble effect
            level.addParticle(ParticleTypes.SMOKE, x, y, z,
                    0.0D, 0.05D, 0.0D); // Gentle smoke upward

            // Acid sizzle sound (lava pop is close to the right vibe)
            level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS,
                    0.3F + random.nextFloat() * 0.5F,
                    0.9F + random.nextFloat() * 0.2F,
                    false);
        }

        if (random.nextInt(100) == 0) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS,
                    0.2F + random.nextFloat() * 0.2F,
                    0.8F + random.nextFloat() * 0.4F,
                    false);
        }
    }

}
