package net.electrisoma.bloodisfuel.fabric.mixin.client;

import net.electrisoma.bloodisfuel.registry.fabric.fluids_utils.FluidBuilderImpl;
import net.electrisoma.bloodisfuel.registry.fabric.fluids_utils.RenderHandlerFactory;
import net.electrisoma.bloodisfuel.fabric.mixin_interfaces.FabricFluidBuilderClient;
import net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder;
import net.electrisoma.bloodisfuel.registry.fluid_utils.BFlowingFluid;
import net.electrisoma.bloodisfuel.multiloader.Env;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;


@SuppressWarnings({"unchecked","rawtypes"})
@Mixin(FluidBuilderImpl.class)
public abstract class FabricFluidBuilderClientMixin extends FluidBuilder<BFlowingFluid, Object> implements FabricFluidBuilderClient {

    @Unique private Supplier<Supplier<RenderType>> renderLayer;
    @Unique private Supplier<RenderHandlerFactory> renderHandler;
    @Unique private int color = -1;

    FabricFluidBuilderClientMixin(AbstractRegistrate owner, Object parent, String name, BuilderCallback callback,
                                  ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction factory) {
        super(owner, parent, name, callback, stillTexture, flowingTexture, factory);
    }

    @Inject(method = "registerClient", at = @At("HEAD"), remap = false)
    private void bloodisfuel$registerClient(CallbackInfo ci) {
        if (this.renderHandler != null) {
            this.renderHandler = () -> (stillTexture, flowingTexture) ->
                    new SimpleFluidRenderHandler(stillTexture, flowingTexture, flowingTexture, this.color);
        }
        this.onRegister(this::registerRenderHandler);
    }

    @Inject(method = "handleClientStuff", at = @At("HEAD"), remap = false)
    private void bloodisfuel$handleClientStuff(CallbackInfo ci) {
        this.renderHandler(() -> SimpleFluidRenderHandler::new);
    }

    @Unique
    protected void registerRenderHandler(BFlowingFluid entry) {
        Env.executeOnClient(() -> () -> {
            final FluidRenderHandler handler = this.renderHandler.get().create(this.stillTexture, this.flowingTexture);
            FluidRenderHandlerRegistry.INSTANCE.register(entry, handler);
            FluidRenderHandlerRegistry.INSTANCE.register(entry.getSource(), handler);
//			ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register((atlasTexture, registry) -> {
//				registry.register(this.stillTexture);
//				registry.register(this.flowingTexture);
//			});
        });
    }

    @Override
    public FabricFluidBuilderClient layer(Supplier<Supplier<RenderType>> layer) {
        if (this.renderLayer == null) {
            this.onRegister(this::registerLayer);
        }
        this.renderLayer = layer;
        return this;
    }

    @Unique
    protected void registerLayer(BFlowingFluid entry) {
        Env.executeOnClient(() -> () -> {
            final RenderType layer = renderLayer.get().get();
            BlockRenderLayerMap.INSTANCE.putFluid(entry, layer);
        });
    }

    @Override
    public FabricFluidBuilderClient renderHandler(Supplier<RenderHandlerFactory> handler) {
        if (this.color != -1) {
            throw new IllegalArgumentException("Can only set either color or render handler factory!");
        }
        this.renderHandler = handler;
        return this;
    }

    @Override
    public FabricFluidBuilderClient color(int color) {
        if (this.renderHandler != null) {
            throw new IllegalArgumentException("Can only set either color or render handler factory!");
        }
        this.color = color;
        return this;
    }
}