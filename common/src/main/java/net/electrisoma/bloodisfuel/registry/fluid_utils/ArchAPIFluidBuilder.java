//package net.electrisoma.bloodisfuel.registry.fluid_utils;
//
//import com.tterrag.registrate.AbstractRegistrate;
//import com.tterrag.registrate.builders.AbstractBuilder;
//import com.tterrag.registrate.builders.BuilderCallback;
//import com.tterrag.registrate.builders.FluidBuilder;
//import com.tterrag.registrate.util.nullness.NonNullFunction;
//import com.tterrag.registrate.util.nullness.NonNullSupplier;
//import com.tterrag.registrate.util.nullness.NonnullType;
//import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
//import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
//import net.minecraft.core.Registry;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.LiquidBlock;
//import net.minecraft.world.level.material.Fluid;
//
//public class ArchAPIFluidBuilder<T extends ArchitecturyFlowingFluid, P>
//        extends AbstractBuilder<Fluid, T, P, ArchAPIFluidBuilder<T, P>> {
//
//
//    public static <P> ArchAPIFluidBuilder<ArchitecturyFlowingFluid.Flowing, P>
//    create(AbstractRegistrate<?> owner, P parent,
//           String name, BuilderCallback callback,
//           ResourceLocation stillTexture, ResourceLocation flowingTexture) {
//        return create(owner, parent, name, callback, stillTexture, flowingTexture, ArchitecturyFlowingFluid.Flowing::new);
//    }
//
//    public ArchAPIFluidBuilder(AbstractRegistrate<?> owner, P parent,
//                               String name, BuilderCallback callback,
//                               ResourceKey<Registry<Fluid>> registryKey) {
//        super(owner, parent, name, callback, registryKey);
//    }
//
//    protected final ResourceLocation stillTexture;
//    protected final ResourceLocation flowingTexture;
//    public final String sourceName;
//    protected final String bucketName;
//    protected final NonNullFunction<ArchitecturyFlowingFluid.Flowing, T> factory;
//
//    protected ArchAPIFluidBuilder(AbstractRegistrate<?> owner, P parent,
//                                      String name, BuilderCallback callback,
//                                      ResourceLocation stillTexture, ResourceLocation flowingTexture,
//                                      NonNullFunction<ArchitecturyFlowingFluid.Flowing, T> factory) {
//        super(owner, parent, "flowing_" + name, callback, BuiltInRegistries.FLUID.key();
//        this.stillTexture = stillTexture;
//        this.flowingTexture = flowingTexture;
//        this.sourceName = name;
//        this.bucketName = name + "_bucket";
//        this.factory = factory;
//
//        String bucketName = this.bucketName;
//        this.properties = p -> p.bucket(() -> owner.get(bucketName, BuiltInRegistries.ITEM.key()).get())
//                .block(() -> owner.<Block, LiquidBlock>get(name, BuiltInRegistries.BLOCK.key()).get());
//    }
//
//    @Override
//    protected T createEntry() {
//        return factory.apply(makeProperties());
//    }
//
//    protected ArchitecturyFlowingFluid.Flowing makeProperties() {
//        NonNullSupplier<? extends ArchitecturyFlowingFluid> source = this.source;
//        ArchitecturyFlowingFluid.Flowing ret =
//                new ArchitecturyFluidAttributes(source, asSupplier(), this.stillTexture, this.flowingTexture);
//        this.properties.accept(ret);
//        return ret;
//    }
//}
