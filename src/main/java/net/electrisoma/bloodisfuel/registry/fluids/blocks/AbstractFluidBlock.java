package net.electrisoma.bloodisfuel.registry.fluids.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Supplier;


@SuppressWarnings("all")
public abstract class AbstractFluidBlock extends LiquidBlock {

    private final Supplier<? extends FlowingFluid> fluid;

    public AbstractFluidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties) {
        super(fluid, properties);
        this.fluid = fluid;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        FluidState fluidState = level.getFluidState(pos);

        if (fluidState.getType() == this.fluid.get()) {

            FluidType fluidType = fluidState.getFluidType();
            float height = (float) entity.getFluidTypeHeight(fluidType);

            if (height > 0.1F) {
                entityInsideProxy(level, pos, entity);
            }
        }
    }

    protected void entityInsideProxy(Level level, BlockPos pos, Entity entity) {

        // break fall damage
        entity.fallDistance = 0.0F;
        if (entity.getDeltaMovement().y < -0.25D) {
            entity.setDeltaMovement(entity.getDeltaMovement().x, -0.25D, entity.getDeltaMovement().z);
        }

        // viscosity
        double mult = thickness();
        if(entity instanceof LivingEntity livingEntity) {
            if (!livingEntity.isNoGravity()) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(mult, mult, mult));
            }
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(mult, mult, mult));
        }

        // extinguish
        if (shouldExtinguishFire() && entity.isOnFire()) {
            entity.extinguishFire();

            level.playSound(
                    null,
                    entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS,
                    1.0F, 1.0F
            );
        }

        // step sound
        if (entity instanceof LivingEntity livingEntity && !level.isClientSide) {
            playStepSound(level, livingEntity);
        }
    }

    protected void playStepSound(Level level, LivingEntity entity) {

        var data = entity.getPersistentData();
        double dx = Math.abs(entity.getX() - data.getDouble("last_x"));
        double dz = Math.abs(entity.getZ() - data.getDouble("last_z"));
        if (dx <= 0.02 && dz <= 0.02) return;

        long currentTick = level.getGameTime();
        if (currentTick - data.getLong("last_sound_tick") >= getStepSoundCooldown(entity)) {
            data.putLong("last_sound_tick", currentTick);
            level.playSound(
                    null,
                    entity.getX(), entity.getY(), entity.getZ(),
                    getStepSound(), getSoundSource(),
                    getStepSoundVolume(), getStepSoundPitch(level));
        }

        data.putDouble("last_x", entity.getX());
        data.putDouble("last_z", entity.getZ());
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.EMPTY;
    }

    protected SoundSource getSoundSource() {
        return SoundSource.BLOCKS;
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

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {

        entity.causeFallDamage(0.0F, 0.0F, level.damageSources().fall());
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {

        if (!entity.isSuppressingBounce())
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
    }

    protected boolean shouldExtinguishFire() {
        return true;
    }

    protected double thickness() {
        return 0.9;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return true;
    }
}
