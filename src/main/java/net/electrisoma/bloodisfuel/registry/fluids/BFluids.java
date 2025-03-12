package net.electrisoma.bloodisfuel.registry.fluids;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.simibubi.create.AllFluids;
import com.tterrag.registrate.builders.FluidBuilder;
import org.antlr.v4.runtime.misc.NotNull;
import org.joml.Vector3f;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.config.BConfigs;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.BModTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;

import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import net.createmod.catnip.theme.Color;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;


@SuppressWarnings("all")
public class BFluids {
    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    static {
        REGISTRATE.setCreativeTab(BModTabs.BASE_CREATIVE_TAB);
    }

    public static final FluidEntry<ForgeFlowingFluid.Flowing> VISCERA =
            REGISTRATE.standardFluid("viscera",
                    SolidRenderedPlaceableFluidType.create(0xFF7350,
                            () -> 1f / 32f * BConfigs.client().visceraTransparencyMultiplier.getF()))
            .lang("Viscera")
            .properties(b -> b.viscosity(1500)
                    .density(500))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BTags.BFluidTags.VISCERA.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BTags.forgeItemTag("buckets/viscera"))
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> BLOOD =
            REGISTRATE.standardFluid("blood",
                    SolidRenderedPlaceableFluidType.create(0xBD3228,
                            () -> 1f / 16f * BConfigs.client().bloodTransparencyMultiplier.getF()))
            .lang("Blood")
            .properties(b -> b.viscosity(1000)
                    .density(500))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BTags.BFluidTags.BLOOD.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BTags.forgeItemTag("buckets/blood"))
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> ENRICHED_BLOOD =
            REGISTRATE.standardFluid("enriched_blood",
                    SolidRenderedPlaceableFluidType.create(0xDD2012,
                                () -> 1f / 8f * BConfigs.client().enrichedBloodTransparencyMultiplier.getF()))
            .lang("Enriched Blood")
            .properties(b -> b
                    .viscosity(1250)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BTags.BFluidTags.ENRICHED_BLOOD.tag)
                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(BTags.forgeItemTag("buckets/enriched_blood"))
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> OIL_ENRICHED_BLOOD =
            REGISTRATE.standardFluid("oil_enriched_blood",
                    SolidRenderedPlaceableFluidType.create(0xDD2012,
                            () -> 1f / 16f * BConfigs.client().oilEnrichedBloodTransparencyMultiplier.getF()))
            .lang("Oil Enriched Blood")
            .properties(b -> b
                    .viscosity(1250)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BTags.BFluidTags.OIL_ENRICHED_BLOOD.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BTags.forgeItemTag("buckets/oil_enriched_blood"))
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> DIESEL_INFUSED_BLOOD =
            REGISTRATE.standardFluid("diesel_infused_blood",
                    SolidRenderedPlaceableFluidType.create(0xDD2012,
                            () -> 1f / 32f * BConfigs.client().dieselInfusedBloodTransparencyMultiplier.getF()))
            .lang("Diesel Infused Blood")
            .properties(b -> b
                    .viscosity(1250)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BTags.BFluidTags.DIESEL_INFUSED_BLOOD.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BTags.forgeItemTag("buckets/diesel_infused_blood"))
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> GASOLINE_INFUSED_BLOOD =
            REGISTRATE.standardFluid("gasoline_infused_blood",
                    SolidRenderedPlaceableFluidType.create(0xDD2012,
                            () -> 1f / 32f * BConfigs.client().gasolineInfusedBloodTransparencyMultiplier.getF()))
            .lang("Gasoline Infused Blood")
            .properties(b -> b
                    .viscosity(1250)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BTags.BFluidTags.GASOLINE_INFUSED_BLOOD.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BTags.forgeItemTag("buckets/gasoline_infused_blood"))
            .build()
            .register();

    public static void register() {}

    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }
        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) { super(properties, stillTexture, flowingTexture); }
        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }
        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }
        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }
}