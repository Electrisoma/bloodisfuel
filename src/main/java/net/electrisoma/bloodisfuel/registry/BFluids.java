package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.config.BConfigs;
import net.electrisoma.bloodisfuel.registry.fluids.blocks.*;
import net.electrisoma.bloodisfuel.registry.fluids.fluids.*;
import net.electrisoma.bloodisfuel.registry.fluids.fluidtypes.*;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.FluidEntry;

import net.minecraft.tags.FluidTags;

import net.minecraftforge.fluids.ForgeFlowingFluid;


public class BFluids {

    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    static {REGISTRATE.setCreativeTab(BModTabs.BASE_CREATIVE_TAB);}

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering fluids for " + BloodIsFuel.NAME);
    }

    public static final FluidEntry<ForgeFlowingFluid.Flowing> VISCERA =
            REGISTRATE.standardFluid("viscera",
                            BloodFluidType.create(0x650B0F,
                                    () -> 1f / 32f * BConfigs.client().visceraTransparencyMultiplier.getF(),"blood")
                    ).lang("Viscera")
                    .properties(b -> b
                            .viscosity(1500)
                            .density(500))
                    .fluidProperties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .tag(FluidTags.WATER)
                    .tag(BTags.BFluidTags.DIVING_FLUID.tag)
                    .tag(BTags.BFluidTags.VISCERA.tag)
                            .source(VisceraFluid.Source::new)
                            .block(VisceraBlock::new).build()
                            .bucket()
                            .tag(BTags.forgeItemTag("buckets/viscera"))
                            .build()
                            .register()
            ;

    public static final FluidEntry<ForgeFlowingFluid.Flowing> BLOOD =
            REGISTRATE.standardFluid("blood",
                            BloodFluidType.create(0x570000,
                                    () -> 1f / 16f * BConfigs.client().bloodTransparencyMultiplier.getF(), "blood")
                    ).lang("Blood")
                    .properties(b -> b
                            .viscosity(1000)
                            .density(500))
                    .fluidProperties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .tag(FluidTags.WATER)
                    .tag(BTags.BFluidTags.DIVING_FLUID.tag)
                    .tag(BTags.BFluidTags.BLOOD.tag)
                            .source(BloodFluid.Source::new)
                            .block(BloodBlock::new).build()
                            .bucket()
                            .tag(BTags.forgeItemTag("buckets/blood"))
                            .build()
                            .register()
            ;

    public static final FluidEntry<ForgeFlowingFluid.Flowing> ENRICHED_BLOOD =
            REGISTRATE.standardFluid("enriched_blood",
                            BloodFluidType.create(0x830000,
                                () -> 1f / 8f * BConfigs.client().enrichedBloodTransparencyMultiplier.getF(), "blood")
                    ).lang("Enriched Blood")
                    .properties(b -> b
                            .viscosity(1250)
                            .density(500))
                    .fluidProperties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .tag(FluidTags.WATER)
                    .tag(BTags.BFluidTags.DIVING_FLUID.tag)
                    .tag(BTags.BFluidTags.ENRICHED_BLOOD.tag)
                            .source(BloodFluid.Source::new)
                            .block(BloodBlock::new).build()
                            .bucket()
                            .tag(BTags.forgeItemTag("buckets/enriched_blood"))
                            .build()
                            .register()
            ;

    public static final FluidEntry<ForgeFlowingFluid.Flowing> OIL_ENRICHED_BLOOD =
            REGISTRATE.standardFluid("oil_enriched_blood",
                            BloodFluidType.create(0x640000,
                                    () -> 1f / 16f * BConfigs.client().oilEnrichedBloodTransparencyMultiplier.getF(),"blood")
                    ).lang("Oil Enriched Blood")
                    .properties(b -> b
                            .viscosity(1250)
                            .density(500))
                    .fluidProperties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .tag(FluidTags.WATER)
                    .tag(BTags.BFluidTags.DIVING_FLUID.tag)
                    .tag(BTags.BFluidTags.OIL_ENRICHED_BLOOD.tag)
                            .source(BloodFluid.Source::new)
                            .block(BloodBlock::new).build()
                            .bucket()
                            .tag(BTags.forgeItemTag("buckets/oil_enriched_blood"))
                            .build()
                            .register()
            ;

    public static final FluidEntry<ForgeFlowingFluid.Flowing> DIESEL_INFUSED_BLOOD =
            REGISTRATE.standardFluid("diesel_infused_blood",
                            BloodFluidType.create(0x640000,
                                    () -> 1f / 32f * BConfigs.client().dieselInfusedBloodTransparencyMultiplier.getF(),"blood")
                    ).lang("Diesel Infused Blood")
                    .properties(b -> b
                            .viscosity(1250)
                            .density(500))
                    .fluidProperties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .tag(FluidTags.WATER)
                    .tag(BTags.BFluidTags.DIVING_FLUID.tag)
                    .tag(BTags.BFluidTags.DIESEL_INFUSED_BLOOD.tag)
                            .source(BloodFluid.Source::new)
                            .block(BloodBlock::new).build()
                            .bucket()
                            .tag(BTags.forgeItemTag("buckets/diesel_infused_blood"))
                            .build()
                            .register()
            ;

    public static final FluidEntry<ForgeFlowingFluid.Flowing> GASOLINE_INFUSED_BLOOD =
            REGISTRATE.standardFluid("gasoline_infused_blood",
                            BloodFluidType.create(0x640000,
                                    () -> 1f / 32f * BConfigs.client().gasolineInfusedBloodTransparencyMultiplier.getF(),"blood")
                    ).lang("Gasoline Infused Blood")
                    .properties(b -> b
                            .viscosity(1250)
                            .density(500))
                    .fluidProperties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .tag(FluidTags.WATER)
                    .tag(BTags.BFluidTags.DIVING_FLUID.tag)
                    .tag(BTags.BFluidTags.GASOLINE_INFUSED_BLOOD.tag)
                            .source(BloodFluid.Source::new)
                            .block(BloodBlock::new).build()
                            .bucket()
                            .tag(BTags.forgeItemTag("buckets/gasoline_infused_blood"))
                            .build()
                            .register()
            ;

    public static final FluidEntry<ForgeFlowingFluid.Flowing> BOILING_BLOOD =
            REGISTRATE.standardFluid("boiling_blood",
                            BoilingBloodFluidType.create(0x640000,
                                    () -> 1f / 32f * BConfigs.client().boilingBloodTransparencyMultiplier.getF(),"blood")
                    ).lang("Boiling Blood")
                    .properties(b -> b
                            .viscosity(1250)
                            .density(500)
                            .temperature(1270)
                            .lightLevel(15))
                    .fluidProperties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .tag(FluidTags.WATER)
                    .tag(BTags.BFluidTags.DIVING_FLUID.tag)
                    .tag(BTags.BFluidTags.BOILING_BLOOD.tag)
                            .source(BoilingBloodFluid.Source::new)
                            .block(BoilingBloodBlock::new).build()
                            .bucket().tag(BTags.forgeItemTag("buckets/boiling_blood"))
                            .build()
                            .register()
            ;


    // for later
    public enum FluidProperties {

        VISCERA(15,16,5),
        BLOOD(7,2,5),
        ENRICHED_BLOOD(10,1.3f,5),

        OIL_ENRICHED_BLOOD(20,0.8f,5),
        DIESEL_INFUSED_BLOOD(35,0.6f,5),
        GASOLINE_INFUSED_BLOOD(20,1.8f,5),

        ;

        public final float strength;
        public final float speed;
        public final int burn_rate;

        FluidProperties(float strength, float speed, int burn_rate){
            this.strength = strength;
            this.speed = speed;
            this.burn_rate = burn_rate;
        }
    }
}