package net.electrisoma.bloodisfuel.api.engine.abstract_engine;

import net.electrisoma.bloodisfuel.api.utils.EngineUtils;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.List;


public abstract class AbstractEngineBlockEntity extends GeneratingKineticBlockEntity implements EngineUtils {
    protected CapacityEnchantedFluidTankBehaviour tank;
    protected Component customName;
    protected ListTag enchantmentTag;
    protected int capacityEnchantLevel;
    protected int fuelTick;

    public AbstractEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        fuelTick++;
        assert level != null;
        if (!level.isClientSide && enabled()) {
            tickFuelUsage(1.0f);
            updateGeneratedRotation();
        }
    }

    @Override
    public float calculateAddedStressCapacity() {
        if (!enabled() || getGeneratedSpeed() == 0)
            return 0;

        float stress = getFuelStress();
        lastCapacityProvided = stress;
        return stress;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        float stressBase = calculateAddedStressCapacity();
        if (Mth.equal(stressBase, 0))
            return added;
        return containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability().cast());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = CapacityEnchantedFluidTankBehaviour.single(this, 1000, 1000);
        behaviours.add(tank);
    }

    @Override public <T> LazyOptional<T> getCapability(Capability<T> cap) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return tank.getCapability().cast();
        return super.getCapability(cap);
    }
    @Override public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }
    @Override protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("CapacityEnchantment", capacityEnchantLevel);
        if (customName != null)
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        if (enchantmentTag != null)
            compound.put("Enchantments", enchantmentTag);
    }
    @Override protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        capacityEnchantLevel = compound.getInt("CapacityEnchantment");
        if (compound.contains("Enchantments"))
            enchantmentTag = compound.getList("Enchantments", Tag.TAG_COMPOUND);
        if (compound.contains("CustomName", 8))
            customName = Component.Serializer.fromJson(compound.getString("CustomName"));
    }

    @Override public int getTick() {
        return fuelTick;
    }
    @Override public SmartFluidTankBehaviour getTank() {
        return tank;
    }
    @Override public SmartBlockEntity self() {
        return this;
    }
    @Override public void playSound() {}

    public FluidStack getFluid() { return tank.getPrimaryHandler().getFluid(); }
    public Component getCustomName() { return customName; }
    public ListTag getEnchantmentTag() { return enchantmentTag; }
    public int getCapacityEnchantLevel() { return capacityEnchantLevel; }

    public void setCustomName(Component name) { this.customName = name; }
    public void setEnchantmentTag(ListTag tag) { this.enchantmentTag = tag; }
    public void setCapacityEnchantLevel(int level) {
        this.capacityEnchantLevel = level;
        tank.getPrimaryHandler().setCapacity(tank.baseCapacity + tank.capacityAddition * level);
    }

    public static class CapacityEnchantedFluidTankBehaviour extends SmartFluidTankBehaviour {
        public final int capacityAddition;
        public final int baseCapacity;

        public CapacityEnchantedFluidTankBehaviour(BehaviourType<SmartFluidTankBehaviour> type, SmartBlockEntity be,
                                                   int tanks, int tankCapacity, boolean enforceVariety, int capacityAddition) {
            super(type, be, tanks, tankCapacity, enforceVariety);
            this.capacityAddition = capacityAddition;
            this.baseCapacity = tankCapacity;
        }

        public static CapacityEnchantedFluidTankBehaviour single(SmartBlockEntity be, int capacity, int capacityAddition) {
            return new CapacityEnchantedFluidTankBehaviour(TYPE, be, 1, capacity, false, capacityAddition);
        }

        @Override
        public void read(CompoundTag compound, boolean clientPacket) {
            super.read(compound, clientPacket);
            if (compound.contains("CapacityEnchantment"))
                getPrimaryHandler().setCapacity(baseCapacity + compound.getInt("CapacityEnchantment") * capacityAddition);
        }
    }
}
