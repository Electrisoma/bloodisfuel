package net.electrisoma.bloodisfuel.content.equipment.bloodextractor;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;
import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import javax.annotation.Nullable;
import java.util.List;

public class BloodExtractorBlockEntity extends KineticBlockEntity implements SyringeUtils, IHaveGoggleInformation {
    private SmartFluidTankBehaviour tank;
    private int cooldown = 0;

    public BloodExtractorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.TYPE, this, 1, 8000, false);
        behaviours.add(tank);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability().cast());
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide)
            return;

        float rpm = Math.abs(getSpeed());

        if (rpm < 16)
            return;

        int tickRate = Mth.clamp((int) (512 / rpm), 1, 20);
        if (cooldown-- > 0)
            return;

        cooldown = tickRate;

        AABB scanBox = new AABB(worldPosition.above());
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, scanBox, LivingEntity::isAlive);

        if (targets.isEmpty())
            return;

        LivingEntity target = targets.getFirst();
        processEntity(target, rpm);
    }

    private void processEntity(LivingEntity entity, float rpm) {
        if (!entity.isAlive()) return;

        assert level != null;
        RegistryAccess access = level.registryAccess();
        SyringeFluidType type = getMatchingFluid(entity, access);
        if (type == null) type = getFallback(access);
        if (type == null) return;

        Fluid fluid = SyringeFluidTypeManager.getFluidFor(type);
        if (fluid == null || fluid == Fluids.EMPTY) return;

        DamageSource source = new DamageSource(access
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.MAGIC));

        if (!entity.isInvulnerableTo(source)) {
            float damage = Mth.clamp(rpm / 16f, 1f, 10f);
            float beforeHealth = entity.getHealth();

            boolean damaged = entity.hurt(source, damage);
            boolean canExtractFluid = !entity.getType().is(BTags.BEntityTags.DOES_NOT_DROP_FLUID.tag);

            if (damaged && !entity.isAlive() && beforeHealth > 0.0f && canExtractFluid) {
                FluidStack extractedFluid = new FluidStack(fluid, 1000);
                int filled = tank.getPrimaryHandler().fill(extractedFluid, IFluidHandler.FluidAction.EXECUTE);

                if (filled > 0) {
                    spawnBloodParticles(level, entity, extractedFluid, access, 8);
                    setChanged();
                    sendData();
                    level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
                }
            }
        }

        setChanged();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putInt("Cooldown", cooldown);
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        cooldown = tag.getInt("Cooldown");
        super.read(tag, clientPacket);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }

    public SmartFluidTankBehaviour getTank() {
        return tank;
    }
}
