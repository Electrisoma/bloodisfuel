package net.electrisoma.bloodisfuel.content.fluids.blocks;

import net.electrisoma.bloodisfuel.api.fluids.blocks.AbstractFluidBlock;
import net.electrisoma.bloodisfuel.infrastructure.data.BDamageSources;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;

import java.util.function.Supplier;


public class BlazingBloodBlock extends AbstractFluidBlock {
    public BlazingBloodBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override public void entityInsideProxy(Level level, BlockPos pos, Entity entity) {
        super.entityInsideProxy(level, pos, entity);
        FluidState fluidState = level.getFluidState(pos);
        float fluidHeight = fluidState.getHeight(level, pos);
        double fluidSurfaceY = pos.getY() + fluidHeight;
        double entityBottomY = entity.getBoundingBox().minY;
        double entityTopY = entity.getBoundingBox().maxY;
        if (!(entityBottomY < fluidSurfaceY && entityTopY > pos.getY())) return;
        if (!level.isClientSide()) {
            if (entity instanceof LivingEntity livingEntity && !livingEntity.fireImmune()) {
                livingEntity.setSecondsOnFire(1);
                livingEntity.hurt(BDamageSources.boiling(level), 4.0F);
            }
            if (entity instanceof ItemEntity item) {
                level.playSound(null, item.getX(), item.getY(), item.getZ(),
                        SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.6F, 2.0F);
                ((ServerLevel) level).sendParticles(ParticleTypes.SMOKE,
                        item.getX(), item.getY() + 0.2, item.getZ(),
                        5, 0.0D, 0.05D, 0.0D, 0.01D);
                item.discard();
            }
            if (entity instanceof ExperienceOrb xpOrb) {
                level.playSound(null, xpOrb.getX(), xpOrb.getY(), xpOrb.getZ(),
                        SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.6F, 2.0F);
                ((ServerLevel) level).sendParticles(ParticleTypes.SMOKE,
                        xpOrb.getX(), xpOrb.getY() + 0.2, xpOrb.getZ(),
                        5, 0.0D, 0.05D, 0.0D, 0.01D);
                xpOrb.discard();
            }
        }
    }
    @Override public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockPos blockpos = pos.above();
        if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
            if (random.nextInt(100) == 0) {
                double d0 = (double)pos.getX() + random.nextDouble();
                double d1 = (double)pos.getY() + 1.0;
                double d2 = (double)pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.LAVA, d0, d1, d2,
                        0.0, 0.0, 0.0);
                level.playLocalSound(d0, d1, d2, SoundEvents.LAVA_POP, SoundSource.BLOCKS,
                        0.2F + random.nextFloat() * 0.2F,
                        0.9F + random.nextFloat() * 0.15F,
                        false
                );
            }
            if (random.nextInt(200) == 0) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(),
                        SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS,
                        0.2F + random.nextFloat() * 0.2F,
                        0.9F + random.nextFloat() * 0.15F,
                        false
                );
            }
        }
    }

    @Override protected boolean shouldExtinguishFire() {
        return false;
    }
    @Override public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}