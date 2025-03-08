package net.electrisoma.bloodisfuel.registry.fluids;

import net.electrisoma.bloodisfuel.registry.BIF_Tags;
import net.electrisoma.bloodisfuel.registry.CreativeTabs;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;

import com.simibubi.create.foundation.utility.Color;
import com.tterrag.registrate.builders.FluidBuilder.FluidTypeFactory;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.electrisoma.bloodisfuel.config.BloodConfigs.client;
import static net.electrisoma.bloodisfuel.BloodIsFuel.REGISTRATE;


@SuppressWarnings("all")
public class BIF_Fluids {
    static {
    REGISTRATE.setCreativeTab(CreativeTabs.BASE_CREATIVE_TAB);
}

    public static final FluidEntry<ForgeFlowingFluid.Flowing>
            VISCERA = REGISTRATE
            .standardFluid("viscera",
                    SolidRenderedPlaceableFluidType.create(0xFF7350,
                            () -> 1f / 32f * client().visceraTransparencyMultiplier.getF()))
            .lang("Viscera")
            .properties(b -> b
                    .viscosity(1500)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BIF_Tags.AllFluidTags.VISCERA.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BIF_Tags.forgeItemTag("buckets/viscera"))
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing>
            BLOOD = REGISTRATE
            .standardFluid("blood",
                    SolidRenderedPlaceableFluidType.create(0xBD3228,
                            () -> 1f / 16f * client().bloodTransparencyMultiplier.getF()))
            .lang("Blood")
            .properties(b -> b
                    .viscosity(1000)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BIF_Tags.AllFluidTags.BLOOD.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BIF_Tags.forgeItemTag("buckets/blood"))
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing>
            ENRICHED_BLOOD = REGISTRATE
            .standardFluid("enriched_blood",
                    SolidRenderedPlaceableFluidType.create(0xDD2012,
                                () -> 1f / 8f * client().enrichedBloodTransparencyMultiplier.getF()))
            .lang("Enriched Blood")
            .properties(b -> b
                    .viscosity(1250)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BIF_Tags.AllFluidTags.ENRICHED_BLOOD.tag)
                    .source(ForgeFlowingFluid.Source::new)
                    .bucket()
                    .tag(BIF_Tags.forgeItemTag("buckets/enriched_blood"))
                    .build()
                    .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing>
            OIL_ENRICHED_BLOOD = REGISTRATE
            .standardFluid("oil_enriched_blood",
                    SolidRenderedPlaceableFluidType.create(0xDD2012,
                            () -> 1f / 16f * client().oilEnrichedBloodTransparencyMultiplier.getF()))
            .lang("Oil Enriched Blood")
            .properties(b -> b
                    .viscosity(1250)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BIF_Tags.AllFluidTags.OIL_ENRICHED_BLOOD.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BIF_Tags.forgeItemTag("buckets/oil_enriched_blood"))
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing>
            DIESEL_INFUSED_BLOOD = REGISTRATE
            .standardFluid("diesel_infused_blood",
                    SolidRenderedPlaceableFluidType.create(0xDD2012,
                            () -> 1f / 32f * client().dieselInfusedBloodTransparencyMultiplier.getF()))
            .lang("Diesel Infused Blood")
            .properties(b -> b
                    .viscosity(1250)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BIF_Tags.AllFluidTags.DIESEL_INFUSED_BLOOD.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BIF_Tags.forgeItemTag("buckets/diesel_infused_blood"))
            .build()
            .register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing>
            GASOLINE_INFUSED_BLOOD = REGISTRATE
            .standardFluid("gasoline_infused_blood",
                    SolidRenderedPlaceableFluidType.create(0xDD2012,
                            () -> 1f / 32f * client().gasolineInfusedBloodTransparencyMultiplier.getF()))
            .lang("Gasoline Infused Blood")
            .properties(b -> b
                    .viscosity(1250)
                    .density(500))
            .fluidProperties(p -> p
                    .levelDecreasePerBlock(2)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .tag(BIF_Tags.AllFluidTags.GASOLINE_INFUSED_BLOOD.tag)
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(BIF_Tags.forgeItemTag("buckets/gasoline_infused_blood"))
            .build()
            .register();

    public static void register() {}

    public static abstract class TintedFluidType extends FluidType {

        protected static final int NO_TINT = 0xffffffff;
        private final ResourceLocation stillTexture;
        private final ResourceLocation flowingTexture;

        public TintedFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties);
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {

                @Override
                public ResourceLocation getStillTexture() {
                    return stillTexture;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return flowingTexture;
                }

                @Override
                public int getTintColor(FluidStack stack) {
                    return TintedFluidType.this.getTintColor(stack);
                }

                @Override
                public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                    return TintedFluidType.this.getTintColor(state, getter, pos);
                }

                @Override
                public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                        int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                    Vector3f customFogColor = TintedFluidType.this.getCustomFogColor();
                    return customFogColor == null ? fluidFogColor : customFogColor;
                }

                @Override
                public void modifyFogRender(Camera camera, FogMode mode, float renderDistance, float partialTick,
                                            float nearDistance, float farDistance, FogShape shape) {
                    float modifier = TintedFluidType.this.getFogDistanceModifier();
                    float baseWaterFog = 96.0f;
                    if (modifier != 1f) {
                        RenderSystem.setShaderFogShape(FogShape.CYLINDER);
                        RenderSystem.setShaderFogStart(-8);
                        RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
                    }
                }

            });
        }

        protected abstract int getTintColor(FluidStack stack);

        protected abstract int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos);

        protected Vector3f getCustomFogColor() {
            return null;
        }

        protected float getFogDistanceModifier() {
            return 1f;
        }

    }

    private static class SolidRenderedPlaceableFluidType extends TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

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