//package net.electrisoma.bloodisfuel.registry.forge.fluid_utils;
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
//import com.tterrag.registrate.util.nullness.NonNullFunction;
//import com.tterrag.registrate.providers.RegistrateLangProvider;
//import com.tterrag.registrate.providers.RegistrateTagsProvider;
//import com.tterrag.registrate.providers.RegistrateItemModelProvider;
//import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
//
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.LiquidBlock;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.core.registries.BuiltInRegistries;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Arrays;
//
//
//@SuppressWarnings("all")
//public class FluidBuilderImpl<T extends ArchitecturyFlowingFluid, P>
//        extends FluidBuilder<T, P>{
//
//    public FluidBuilderImpl(AbstractRegistrate<?> owner, P parent,
//                            String name, BuilderCallback callback,
//                            ResourceLocation stillTexture, ResourceLocation flowingTexture,
//                            NonNullFunction<ArchitecturyFluidAttributes, T> factory) {
//        super(owner, parent, name, callback, stillTexture, flowingTexture, factory);
//    }
//
//    @SafeVarargs
//    public final FluidBuilder<T, P> tag(TagKey<Fluid>... tags) {
//        FluidBuilder<T, P> ret = this.tag(ProviderType.FLUID_TAGS, tags);
//        if (this.tags.isEmpty()) {
//            ret.getOwner().<RegistrateTagsProvider<Fluid>, Fluid>setDataGenerator(ret.sourceName, getRegistryKey(), ProviderType.FLUID_TAGS,
//                    prov -> this.tags.stream()
//                            .map(prov::addTag)
//                            .forEach(p -> p.add(BuiltInRegistries.FLUID.getResourceKey(this.getSource())
//                                    .orElseThrow())))
//            ;
//        }
//        this.tags.addAll(Arrays.asList(tags));
//        return ret;
//    }
//
//    @Override
//    public FluidBuilder<T, P> defaultLang() {
//        return lang(this::makeDescriptionId, RegistrateLangProvider.toEnglishName(this.sourceName));
//    }
//
//    @Override
//    public FluidBuilder<T, P> lang(String name) {
//        return lang(this::makeDescriptionId, name);
//    }
//
//    @Override
//    public BlockBuilder<ArchitecturyLiquidBlock, FluidBuilder<T, P>> block() {
//        return null;
//    }
//
////    @Override
////    public BlockBuilder<ArchitecturyLiquidBlock, FluidBuilder<T, P>> block() {
////        return block(LiquidBlock::new);
////    }
//
//    @Override
//    protected <B extends Block> void
//    acceptBlockstate(@NotNull DataGenContext<Block, B> ctx, @NotNull RegistrateBlockstateProvider prov) {
//        prov.simpleBlock(ctx.get(), prov.models().getBuilder(this.sourceName).texture("particle", this.stillTexture));
//    }
//
//    @Override
//    protected <I extends Item> void
//    acceptItemModel(DataGenContext<Item, I> ctx, @NotNull RegistrateItemModelProvider prov) {
//        prov.generated(ctx, new ResourceLocation(this.getOwner().getModid(), "item/" + this.bucketName));
//    }
//}