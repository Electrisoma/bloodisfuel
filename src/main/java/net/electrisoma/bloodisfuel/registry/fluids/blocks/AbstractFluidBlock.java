package net.electrisoma.bloodisfuel.registry.fluids.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

import net.minecraftforge.fluids.FluidType;

import java.util.function.Supplier;


@SuppressWarnings({"unused", "deprecation", "RedundantSuppression"})
public abstract class AbstractFluidBlock extends LiquidBlock {
    private final Supplier<? extends FlowingFluid> fluid;

    public AbstractFluidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
        this.fluid = fluid;
    }

    // entity logic
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        FluidState fluidState = level.getFluidState(pos);

        if (fluidState.getType() == this.fluid.get()) {
            FluidType fluidType = fluidState.getFluidType();
            float height = (float) entity.getFluidTypeHeight(fluidType);
            if (height > 0.1F) entityInsideProxy(level, pos, entity);
        }
    }
    public void entityInsideProxy(Level level, BlockPos pos, Entity entity) {
        FluidState fluidState = level.getFluidState(pos);
        float fluidHeight = fluidState.getHeight(level, pos);
        double fluidSurfaceY = pos.getY() + fluidHeight;

        double entityBottomY = entity.getBoundingBox().minY;
        double entityTopY = entity.getBoundingBox().maxY;

        if (entityBottomY < fluidSurfaceY && entityTopY > pos.getY()) {

            entity.fallDistance = 0.0F;
            if (entity.getDeltaMovement().y < -0.25D)
                entity.setDeltaMovement(entity.getDeltaMovement().x, -0.25D, entity.getDeltaMovement().z);

            double mult = thickness();
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(mult, mult, mult));

            if (shouldExtinguishFire() && entity.isOnFire()) {
                entity.extinguishFire();
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            if (entity instanceof LivingEntity livingEntity && !level.isClientSide) playStepSound(level, livingEntity);
        }
    }
    @Override public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return true;
    }

    // fall damage logic
    @Override public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(0.0F, 0.0F, level.damageSources().fall());
    }
    @Override public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (!entity.isSuppressingBounce())
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
    }

    // controllable variables
    protected void playStepSound(Level level, LivingEntity entity) {
        var data = entity.getPersistentData();
        double dx = Math.abs(entity.getX() - data.getDouble("last_x"));
        double dz = Math.abs(entity.getZ() - data.getDouble("last_z"));
        if (dx <= 0.02 && dz <= 0.02) return;

        long currentTick = level.getGameTime();
        if (currentTick - data.getLong("last_sound_tick") >= getStepSoundCooldown(entity)) {
            data.putLong("last_sound_tick", currentTick);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    getStepSound(), getSoundSource(), getStepSoundVolume(), getStepSoundPitch(level));
        }
        data.putDouble("last_x", entity.getX());
        data.putDouble("last_z", entity.getZ());
    }
    protected SoundSource getSoundSource() {
        return SoundSource.BLOCKS;
    }
    protected SoundEvent getStepSound() {
        return SoundEvents.EMPTY;
    }
    protected float getStepSoundVolume() {
        return 1F;
    }
    protected float getStepSoundPitch(Level level) {
        return 1F;
    }
    protected int getStepSoundCooldown(LivingEntity entity) {
        return 20;
    }
    protected double thickness() {
        return 0.9;
    }
    protected boolean shouldExtinguishFire() {
        return true;
    }
}
