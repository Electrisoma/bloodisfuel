package net.electrisoma.bloodisfuel.registry;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.registry.registries.RegistrySupplier;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.fluid_utils.*;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;


@SuppressWarnings("unchecked")
public class BFluids {

    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    public static final ArchitecturyFluidAttributes BLOOD_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(() -> BLOOD, () -> BLOOD_FLOWING);

    public static final RegistrySupplier<Fluid> BLOOD = BFluids.register("blood", () -> new ArchitecturyFlowingFluid.Source(BLOOD_ATTRIBUTES));

    public static final RegistrySupplier<Fluid> BLOOD_FLOWING = BFluids.register("blood_flowing", () -> new ArchitecturyFlowingFluid.Flowing(BLOOD_ATTRIBUTES));
    public static final RegistrySupplier<LiquidBlock> BLOOD_BLOCK = BBlocks.register("blood", () -> new ArchitecturyLiquidBlock((Supplier<? extends FlowingFluid>)
            BLOOD, BlockBehaviour.Properties.copy(Blocks.WATER)));

//    public static final RegistryEntry<ArchitecturyFlowingFluid.Flowing>
//            VISCERA = standardFluid("viscera")
//                    .lang("Viscera")
//                    .fluidProperties(p -> p
//                            .levelDecreasePerBlock(2)
//                            .tickRate(25)
//                            .flowSpeed(3)
//                            .blastResistance(100f)
//                    ).tag(BTags.AllFluidTags.VISCERA.tag,
//                            forgeTag("viscera"),
//                            fabricTag("viscera")
//                    )
//                    .block(ArchitecturyLiquidBlock::new).build()
//                    .transform()
//                    .register()
//            ;
//
//    public static final RegistryEntry<ArchitecturyFlowingFluid.Flowing>
//            BLOOD = standardFluid("blood")
//                    .lang("Blood")
//                    .fluidProperties(p -> p
//                            .levelDecreasePerBlock(2)
//                            .tickRate(25)
//                            .flowSpeed(3)
//                            .blastResistance(100f)
//                    ).tag(BTags.AllFluidTags.BLOOD.tag,
//                            forgeTag("blood"),
//                            fabricTag("blood"))
//                    .block(ArchitecturyLiquidBlock::new).build()
//                    .register()
//            ;
//
//    public static final RegistryEntry<ArchitecturyFlowingFluid.Flowing>
//            ENRICHED_BLOOD = standardFluid("enriched_blood")
//                    .lang("Enriched Blood")
//                    .fluidProperties(p -> p
//                            .levelDecreasePerBlock(2)
//                            .tickRate(25)
//                            .flowSpeed(3)
//                            .blastResistance(100f)
//                    ).tag(BTags.AllFluidTags.ENRICHED_BLOOD.tag,
//                            forgeTag("enriched_blood"),
//                            fabricTag("enriched_blood")
//                    )
//                    .block(ArchitecturyLiquidBlock::new).build()
//                    .register()
//            ;
//
//    public static final RegistryEntry<ArchitecturyFlowingFluid.Flowing>
//            OIL_ENRICHED_BLOOD = standardFluid("oil_enriched_blood")
//                    .lang("Oil Enriched Blood")
//                    .fluidProperties(p -> p
//                            .levelDecreasePerBlock(2)
//                            .tickRate(25)
//                            .flowSpeed(3)
//                            .blastResistance(100f)
//                    ).tag(BTags.AllFluidTags.OIL_ENRICHED_BLOOD.tag,
//                            forgeTag("oil_enriched_blood"),
//                            fabricTag("oil_enriched_blood")
//                    )
//                    .block(HotLiquidBlock::new).build()
//                    .register()
//            ;
//
//    public static final RegistryEntry<ArchitecturyFlowingFluid.Flowing>
//            GASOLINE_INFUSED_BLOOD = standardFluid("gasoline_infused_blood")
//                    .lang("Gasoline Infused Blood")
//                    .fluidProperties(p -> p
//                            .levelDecreasePerBlock(2)
//                            .tickRate(25)
//                            .flowSpeed(3)
//                            .blastResistance(100f)
//                    ).tag(BTags.AllFluidTags.GASOLINE_INFUSED_BLOOD.tag,
//                            forgeTag("gasoline_infused_blood"),
//                            fabricTag("gasoline_infused_blood")
//                    )
//                    .block(ArchitecturyLiquidBlock::new).build()
//                    .register()
//            ;
//
//    public static final RegistryEntry<ArchitecturyFlowingFluid.Flowing>
//            DIESEL_INFUSED_BLOOD = standardFluid("diesel_infused_blood")
//                    .lang("Diesel Infused Blood")
//                    .fluidProperties(p -> p
//                            .tickRate(25)
//                            .flowSpeed(3)
//                            .blastResistance(100f)
//                    ).tag(BTags.AllFluidTags.DIESEL_INFUSED_BLOOD.tag,
//                            forgeTag("diesel_infused_blood"),
//                            fabricTag("diesel_infused_blood")
//                    )
//                    .block(ArchitecturyLiquidBlock::new).build()
//                    .register()
//            ;

    // boiling blood
    // blazing stuff idk
    // extra mob blood types


//    private static <T extends ArchitecturyFlowingFluid, P> FluidBuilder<ArchitecturyFlowingFluid.Flowing, Object>
//    createFluid(String name, NonNullFunction<ArchitecturyFluidAttributes, T> fac) {
//        ResourceLocation stillTex = BloodIsFuel.asResource("fluid/" + name + "_still");
//        ResourceLocation flowingTex = BloodIsFuel.asResource("fluid/" + name + "_flow");
//        return REGISTRATE.entry(name, cb -> FluidBuilder.create(REGISTRATE, regSelf(REGISTRATE), name, cb, stillTex, flowingTex));
//    }
//
//    private static <P> FluidBuilder<ArchitecturyFlowingFluid.Flowing, P>
//    standardFluid(String name) {
//        return (FluidBuilder<ArchitecturyFlowingFluid.Flowing, P>) createFluid(name, ArchitecturyFlowingFluid.Flowing::new);
//    }
//
//    private static <S> S regSelf(CreateRegistrate registrate) {
//        return (S) BFluids.REGISTRATE;
//    }
//
//    private static TagKey<Fluid> forgeTag(String path) {
//        return TagKey.create(Registries.FLUID, new ResourceLocation("forge", path));
//    }
//
//    private static TagKey<Fluid> fabricTag(String path) {
//        return TagKey.create(Registries.FLUID, new ResourceLocation("c", path));
//    }
    public static void register() {
        // load the class and register everything
        BloodIsFuel.LOGGER.info("Registering fluids for " + BloodIsFuel.NAME);
    }

}
