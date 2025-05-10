package net.electrisoma.bloodisfuel.registry.fluids.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

import java.util.function.Supplier;


public class VisceraBlock extends AbstractFluidBlock {

    public VisceraBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void entityInsideProxy(Level level, BlockPos pos, Entity entity) {
        super.entityInsideProxy(level, pos, entity);

        if (!level.isClientSide && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
        }
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SLIME_SQUISH;
    }

    @Override
    protected float getStepSoundVolume() {
        return 0.2F;
    }

    @Override
    protected float getStepSoundPitch(Level level) {
        return 0.2F + level.random.nextFloat() * 0.3F;
    }

    @Override
    protected int getStepSoundCooldown(LivingEntity entity) {
        return 30 + entity.level().random.nextInt(20); // 40–60 ticks
    }

    @Override
    protected double thickness() {
        return 0.6;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}