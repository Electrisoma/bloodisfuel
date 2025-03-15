package net.electrisoma.bloodisfuel.registry.fluids;

import static net.minecraft.world.item.Items.BUCKET;
import static net.minecraft.world.item.Items.GLASS_BOTTLE;
import static net.minecraft.world.item.Items.HONEY_BOTTLE;

import com.mojang.blaze3d.shaders.FogShape;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents.ColorData;
import net.electrisoma.bloodisfuel.config.BConfigs;
import net.electrisoma.bloodisfuel.registry.BCreativeTabs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;

import net.minecraft.network.chat.Component;

import static com.simibubi.create.Create.REGISTRATE;


@SuppressWarnings({"UnstableApiUsage", "unused"})
public class BFluids {
    static {
        REGISTRATE.setCreativeTab(BCreativeTabs.getBaseTabKey());
    }

    public static final FluidEntry<SimpleFlowableFluid.Flowing>
            VISCERA = REGISTRATE
            .standardFluid("viscera")
            .lang("Viscera")
            .fluidProperties(p -> p
                    .tickRate(25)
                    .flowSpeed(3)
                    .blastResistance(100f))
            .fluidAttributes(() -> new CreateAttributeHandler("block.bloodisfuel.viscera", 2000, 1400))
            .onRegisterAfter(Registries.ITEM, viscera -> {
                Fluid source = viscera.getSource();
                // transfer values
                FluidStorage.combinedItemApiProvider(source.getBucket()).register(context ->
                        new FullItemFluidStorage(context, bucket ->
                                ItemVariant.of(BUCKET), FluidVariant.of(source), FluidConstants.BUCKET));
                FluidStorage.combinedItemApiProvider(BUCKET).register(context ->
                        new EmptyItemFluidStorage(context, bucket ->
                                ItemVariant.of(source.getBucket()), source, FluidConstants.BUCKET));
            })
            .register();



    public static void register() {
        FogEvents.RENDER_FOG.register(BFluids::getFogDensity);
        FogEvents.SET_COLOR.register(BFluids::getFogColor);
    }

    public static boolean getFogDensity
            (FogRenderer.FogMode mode, FogType type,
             Camera camera, float partialTick,
             float renderDistance, float nearDistance,
             float farDistance, FogShape shape,
             FogEvents.FogData fogData) {

        Level level = Minecraft.getInstance().level;
        BlockPos blockPos = camera.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (camera.getPosition().y >= blockPos.getY() + fluidState.getHeight(level, blockPos))
            return false;
        Fluid fluid = fluidState.getType();
        Entity entity = camera.getEntity();

        if (BFluids.VISCERA.get()
                .isSame(fluid)) {
            fogData.scaleFarPlaneDistance(1f / 32f * BConfigs.client().visceraTransparencyMultiplier.getF());
            return true;
        }


        if (entity.isSpectator())
            return false;
        return false;
    }

    public static void getFogColor(ColorData event, float partialTicks) {
        Camera info = event.getCamera();
        Level level = Minecraft.getInstance().level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos))
            return;

        Fluid fluid = fluidState.getType();

        if (BFluids.VISCERA.get()
                .isSame(fluid)) {
            event.setRed(98 / 255f);
            event.setGreen(32 / 255f);
            event.setBlue(32 / 255f);
            return;
        }
    }

    private record CreateAttributeHandler(Component name, int viscosity, boolean lighterThanAir) implements FluidVariantAttributeHandler {
        private CreateAttributeHandler(String key, int viscosity, int density) {
            this(Component.translatable(key), viscosity, density <= 0);
        }

        public CreateAttributeHandler(String key) {
            this(key, FluidConstants.WATER_VISCOSITY, 1000);
        }

        @Override
        public Component getName(FluidVariant fluidVariant) {
            return name.copy();
        }

        @Override
        public int getViscosity(FluidVariant variant, @Nullable Level world) {
            return viscosity;
        }

        @Override
        public boolean isLighterThanAir(FluidVariant variant) {
            return lighterThanAir;
        }
    }
}