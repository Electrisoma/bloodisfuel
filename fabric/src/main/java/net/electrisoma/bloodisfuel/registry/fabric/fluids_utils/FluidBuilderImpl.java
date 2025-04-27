//package net.electrisoma.bloodisfuel.registry.fabric.fluids_utils;
//
//import dev.architectury.core.block.ArchitecturyLiquidBlock;
//import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
//import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
//import net.electrisoma.bloodisfuel.registry.fluid_utils.FluidBuilder;
//
//import com.tterrag.registrate.AbstractRegistrate;
//import com.tterrag.registrate.builders.BlockBuilder;
//import com.tterrag.registrate.providers.ProviderType;
//import com.tterrag.registrate.builders.BuilderCallback;
//import com.tterrag.registrate.providers.DataGenContext;
//import com.tterrag.registrate.util.entry.RegistryEntry;
//import com.tterrag.registrate.util.nullness.NonNullConsumer;
//import com.tterrag.registrate.util.nullness.NonNullFunction;
//import com.tterrag.registrate.util.nullness.NonNullSupplier;
//import com.tterrag.registrate.util.nullness.NonNullBiFunction;
//import com.tterrag.registrate.providers.RegistrateTagsProvider;
//import com.tterrag.registrate.providers.RegistrateLangProvider;
//import com.tterrag.registrate.mixin.accessor.FluidBlockAccessor;
//import com.tterrag.registrate.providers.RegistrateItemModelProvider;
//import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
//
//import net.minecraft.Util;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//
//import java.util.Arrays;
//
//
//@SuppressWarnings("unused")
//public class FluidBuilderImpl<T
//        extends ArchitecturyFlowingFluid, P>
//        extends FluidBuilder<T, P> {
//
//    protected final NonNullSupplier<BFluidData.Builder> attributes;
//    private NonNullConsumer<BFluidData.Builder> attributesCallback = $ -> {};
//
//    public FluidBuilderImpl(AbstractRegistrate<?> owner, P parent,
//                            String name, BuilderCallback callback,
//                            ResourceLocation stillTexture, ResourceLocation flowingTexture,
//                            NonNullFunction<ArchitecturyFluidAttributes, T> factory) {
//        super(owner, parent, name, callback, stillTexture, flowingTexture, factory);
//        this.attributes = BFluidData.Builder::new;
//    }
//
//    @SafeVarargs
//    public final FluidBuilder<T, P>
//    tag(TagKey<Fluid>... tags) {
//        FluidBuilder<T, P> ret = this.tag(ProviderType.FLUID_TAGS, tags);
//        if (this.tags.isEmpty()) {
//            ret.getOwner().<RegistrateTagsProvider<Fluid>, Fluid>setDataGenerator(ret.sourceName, getRegistryKey(), ProviderType.FLUID_TAGS,
//                    prov -> this.tags.stream().map(prov::addTag).forEach(p -> p.add(this.getSource())));
//        }
//        this.tags.addAll(Arrays.asList(tags));
//        return ret;
//    }
//
//    public FluidBuilderImpl<T, P>
//    attributes(NonNullConsumer<BFluidData.Builder> cons) {
//        this.attributesCallback = this.attributesCallback.andThen(cons);
//        return this;
//    }
//
//    @Override
//    public BlockBuilder<ArchitecturyLiquidBlock, FluidBuilder<T, P>>
//    block() {
//        return block1((fluid, settings) -> (ArchitecturyLiquidBlock) FluidBlockAccessor.callInit(fluid, settings));
//    }
//
//    @Override
//    protected <B extends Block> void
//    acceptBlockstate(DataGenContext<Block, B> ctx, RegistrateBlockstateProvider prov) {
//        prov.simpleBlock(ctx.get(), prov.models().getBuilder(this.sourceName).texture("particle", this.stillTexture));
//    }
//
//    @Override
//    protected <I extends Item> void
//    acceptItemModel(DataGenContext<Item, I> ctx, RegistrateItemModelProvider prov) {
//        prov.generated(ctx, new ResourceLocation(this.getOwner().getModid(), "item/" + this.bucketName));
//    }
//
//    public <B extends ArchitecturyLiquidBlock> BlockBuilder<B, FluidBuilder<T, P>>
//    block1(NonNullBiFunction<? extends T, BlockBehaviour.Properties, ? extends B> factory) {
//        return block((supplier, settings) -> (
//                (NonNullBiFunction<T, BlockBehaviour.Properties, ? extends B>) factory).apply(supplier.get(), settings)
//        );
//    }
//
//    @Override
//    public FluidBuilder<T, P> defaultLang() {
//        return lang(this::makeDescriptionId, RegistrateLangProvider.toEnglishName(sourceName));
//    }
//
//    @Override
//    public FluidBuilder<T, P> lang(String name) {
//        return lang(this::makeDescriptionId, name);
//    }
//
//    @Override
//    protected ArchitecturyFluidAttributes makeProperties() {
//        BFluidData.Builder attributes = this.attributes.get();
//        RegistryEntry<Block> block = getOwner().getOptional(this.sourceName, Registries.BLOCK);
//        this.attributesCallback.accept(attributes);
//        attributes.translationKey(
//                Util.makeDescriptionId("fluid", new ResourceLocation(getOwner().getModid(), this.sourceName))
//        );
//        return super.makeProperties();
//    }
//
//    @Override
//    public RegistryEntry<T> register() {
//        RegistryEntry<T> ret = super.register();
//        this.registerClient();
//        return ret;
//    }
//
//    public void registerClient() {
//    }
//
//    public void handleClientStuff() {
//    }
//
//}