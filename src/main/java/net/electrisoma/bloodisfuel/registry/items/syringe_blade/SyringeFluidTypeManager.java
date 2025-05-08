package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import com.simibubi.create.Create;
import net.electrisoma.bloodisfuel.registry.BFluids;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;


@SuppressWarnings("unused")
public enum SyringeFluidTypeManager {

    VISCERA(BFluids.VISCERA.get(), 0xDF4416, new MobEffectInstance(MobEffects.WEAKNESS, 60, 1)),
    BLOOD(BFluids.BLOOD.get(), 0xAA0000, new MobEffectInstance(MobEffects.WEAKNESS, 60, 1)),
    ENRICHED_BLOOD(BFluids.ENRICHED_BLOOD.get(), 0xE11313, new MobEffectInstance(MobEffects.WEAKNESS, 60, 1)),

    POTION(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(Create.ID, "potion")), 0x9966FF, null),
    EMPTY(null, 0xBD3228, null);

    private final Fluid fluid;
    private int color;
    private final MobEffectInstance defaultEffect;

    SyringeFluidTypeManager(Fluid fluid, int color, MobEffectInstance defaultEffect) {
        this.fluid = fluid;
        this.color = color;
        this.defaultEffect = defaultEffect;
    }

    public static SyringeFluidTypeManager fromFluid(FluidStack fluidStack) {
        if (fluidStack.isEmpty()) return EMPTY;
        Fluid fluid = fluidStack.getFluid();

        for (SyringeFluidTypeManager type : values()) {
            if (type.fluid != null && type.fluid.equals(fluid)) return type;
        }

        return EMPTY;
    }

    public int getColor(FluidStack stack) {

        if (this == POTION) {
            CompoundTag tag = stack.getOrCreateTag();
            this.color = PotionUtils.getColor(PotionUtils.getAllEffects(tag)) | 0xff000000;
            return color;
        }

        return color;
    }

    public List<MobEffectInstance> getEffects(FluidStack fluidStack) {
        if (this == POTION && fluidStack.hasTag()) {
            CompoundTag tag = fluidStack.getTag();

            return PotionUtils.getAllEffects(tag);
        }

        return defaultEffect == null
                ? Collections.emptyList()
                : Collections.singletonList(new MobEffectInstance(defaultEffect));
    }

    public boolean isPotionType() {
        return this == POTION;
    }
}