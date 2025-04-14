package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.multiloader.RegistryPlatform;
import net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BLiquidBlock;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;
import net.electrisoma.bloodisfuel.registry.fluid_utils.liquid_blocks.BloodLiquidBlock;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;


@SuppressWarnings("unchecked")
public class BFluids {

    public static void register() {
        // load the class and register everything
        BloodIsFuel.LOGGER.info("Registering fluids for " + BloodIsFuel.NAME);
    }

    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    public static final RegistryEntry<BFlowingFluid.Flowing> VISCERA =
            standardFluid("viscera")
                    .lang("Viscera")
                    .properties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .flowSpeed(3)
                            .blastResistance(100f)
                    ).tag(BTags.AllFluidTags.VISCERA.tag,
                            forgeTag("viscera"),
                            fabricTag("viscera")
                    )
                    .block(BloodLiquidBlock::new).build()
                    .transform(RegistryPlatform::doFluidBuilderTransforms)
                    .register();

    public static final RegistryEntry<BFlowingFluid.Flowing> BLOOD =
            standardFluid("blood")
                    .lang("Blood")
                    .properties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .flowSpeed(3)
                            .blastResistance(100f)
                    ).tag(BTags.AllFluidTags.BLOOD.tag,
                            forgeTag("blood"),
                            fabricTag("blood")
                    )
                    .block(BloodLiquidBlock::new).build()
                    .transform(RegistryPlatform::doFluidBuilderTransforms)
                    .register();

    public static final RegistryEntry<BFlowingFluid.Flowing> ENRICHED_BLOOD =
            standardFluid("enriched_blood")
                    .lang("Enriched Blood")
                    .properties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .flowSpeed(3)
                            .blastResistance(100f)
                    ).tag(BTags.AllFluidTags.ENRICHED_BLOOD.tag,
                            forgeTag("enriched_blood"),
                            fabricTag("enriched_blood")
                    )
                    .block(BloodLiquidBlock::new).build()
                    .transform(RegistryPlatform::doFluidBuilderTransforms)
                    .register();

    public static final RegistryEntry<BFlowingFluid.Flowing> OIL_ENRICHED_BLOOD =
            standardFluid("oil_enriched_blood")
                    .lang("Oil Enriched Blood")
                    .properties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .flowSpeed(3)
                            .blastResistance(100f)
                    ).tag(BTags.AllFluidTags.OIL_ENRICHED_BLOOD.tag,
                            forgeTag("oil_enriched_blood"),
                            fabricTag("oil_enriched_blood")
                    )
                    .block(BloodLiquidBlock::new).build()
                    .transform(RegistryPlatform::doFluidBuilderTransforms)
                    .register();

    public static final RegistryEntry<BFlowingFluid.Flowing> GASOLINE_INFUSED_BLOOD =
            standardFluid("gasoline_infused_blood")
                    .lang("Gasoline Infused Blood")
                    .properties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .flowSpeed(3)
                            .blastResistance(100f)
                    ).tag(BTags.AllFluidTags.GASOLINE_INFUSED_BLOOD.tag,
                            forgeTag("gasoline_infused_blood"),
                            fabricTag("gasoline_infused_blood")
                    )
                    .block(BloodLiquidBlock::new).build()
                    .transform(RegistryPlatform::doFluidBuilderTransforms)
                    .register();

    public static final RegistryEntry<BFlowingFluid.Flowing> DIESEL_INFUSED_BLOOD =
            standardFluid("diesel_infused_blood")
                    .lang("Diesel Infused Blood")
                    .properties(p -> p
                            .levelDecreasePerBlock(2)
                            .tickRate(25)
                            .flowSpeed(3)
                            .blastResistance(100f)
                    ).tag(BTags.AllFluidTags.DIESEL_INFUSED_BLOOD.tag,
                            forgeTag("diesel_infused_blood"),
                            fabricTag("diesel_infused_blood")
                    )
                    .block(BloodLiquidBlock::new).build()
                    .transform(RegistryPlatform::doFluidBuilderTransforms)
                    .register();

    // boiling blood
    // blazing stuff idk
    // extra mob blood types

    private static <T extends BFlowingFluid, P> FluidBuilder<T, P>
    createFluid(String name, NonNullFunction<BFlowingFluid.Properties, T> fac) {
        ResourceLocation stillTex = BloodIsFuel.asResource("fluid/" + name + "_still");
        ResourceLocation flowingTex = BloodIsFuel.asResource("fluid/" + name + "_flow");
        return REGISTRATE.entry(name, cb ->
                FluidBuilder.create(REGISTRATE, regSelf(), name, cb, stillTex, flowingTex, fac));
    }

    private static <P> FluidBuilder<BFlowingFluid.Flowing, P>
    standardFluid(String name) {
        return createFluid(name, BFlowingFluid.Flowing::new);
    }

    private static <S> S regSelf() {
        return (S) BFluids.REGISTRATE;
    }

    private static TagKey<Fluid> forgeTag(String path) {
        return TagKey.create(Registries.FLUID, new ResourceLocation("forge", path));
    }

    private static TagKey<Fluid> fabricTag(String path) {
        return TagKey.create(Registries.FLUID, new ResourceLocation("c", path));
    }
}
