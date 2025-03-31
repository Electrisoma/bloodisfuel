package net.electrisoma.bloodisfuel.registry.fluid_utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.registries.Registries;

import net.electrisoma.bloodisfuel.base.utils.LazySupplier;
import net.electrisoma.bloodisfuel.multiloader.RegistryPlatform;

public abstract class FluidBuilder<T extends BFlowingFluid, P> extends AbstractBuilder<Fluid, T, P, FluidBuilder<T, P>> {

    public static <P> FluidBuilder<BFlowingFluid.Flowing, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        return create(owner, parent, name, callback, stillTexture, flowingTexture, BFlowingFluid.Flowing::new);
    }

    public static <T extends BFlowingFluid, P> FluidBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
                                                                           NonNullFunction<BFlowingFluid.Properties, T> factory) {

        return RegistryPlatform.createFluidBuilder(owner, parent, name, callback, stillTexture, flowingTexture, factory)
                .defaultLang().defaultSource().defaultBlock().defaultBucket();
    }

    protected final ResourceLocation stillTexture;
    protected final ResourceLocation flowingTexture;
    public final String sourceName;
    protected final String bucketName;
    protected final NonNullFunction<BFlowingFluid.Properties, T> factory;

    @Nullable
    private Boolean defaultSource, defaultBlock, defaultBucket;
    private NonNullConsumer<BFlowingFluid.Properties> properties;
    @Nullable
    private NonNullSupplier<? extends BFlowingFluid> source;
    protected List<TagKey<Fluid>> tags = new ArrayList<>();

    protected FluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
                           NonNullFunction<BFlowingFluid.Properties, T> factory) {
        super(owner, parent, "flowing_" + name, callback, Registries.FLUID);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.sourceName = name;
        this.bucketName = name + "_bucket";
        this.factory = factory;

        String bucketName = this.bucketName;
        this.properties = p -> p.bucket(() -> owner.get(bucketName, Registries.ITEM).get())
                .block(() -> owner.<Block, LiquidBlock>get(name, Registries.BLOCK).get());
    }

    public FluidBuilder<T, P> properties(NonNullConsumer<BFlowingFluid.Properties> cons) {
        properties = properties.andThen(cons);
        return this;
    }

    public abstract FluidBuilder<T, P> defaultLang();

    public abstract FluidBuilder<T, P> lang(String name);

    public FluidBuilder<T, P> defaultSource() {
        if (this.defaultSource != null) {
            throw new IllegalStateException("Cannot set a default source after a custom source has been created");
        }
        this.defaultSource = true;
        return this;
    }

    public FluidBuilder<T, P> source(NonNullFunction<BFlowingFluid.Properties, ? extends BFlowingFluid> factory) {
        this.defaultSource = false;
        this.source = new LazySupplier<>(() -> factory.apply(makeProperties()));
        return this;
    }

    public FluidBuilder<T, P> defaultBlock() {
        if (this.defaultBlock != null) {
            throw new IllegalStateException("Cannot set a default block after a custom block has been created");
        }
        this.defaultBlock = true;
        return this;
    }

    public abstract BlockBuilder<LiquidBlock, FluidBuilder<T, P>> block();

    public <B extends LiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block(NonNullBiFunction<NonNullSupplier<? extends T>, BlockBehaviour.Properties, ? extends B> factory) {
        if (this.defaultBlock == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to block/noBlock per builder allowed");
        }
        this.defaultBlock = false;
        NonNullSupplier<T> supplier = asSupplier();
        return getOwner().<B, FluidBuilder<T, P>>block(this, sourceName, p -> factory.apply(supplier, p))
                .properties(p -> BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable())
                .blockstate(this::acceptBlockstate);
    }

    protected abstract <B extends Block> void acceptBlockstate(DataGenContext<Block, B> ctx, RegistrateBlockstateProvider prov);

    @Beta
    public FluidBuilder<T, P> noBlock() {
        if (this.defaultBlock == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to block/noBlock per builder allowed");
        }
        this.defaultBlock = false;
        return this;
    }

    public FluidBuilder<T, P> defaultBucket() {
        if (this.defaultBucket != null) {
            throw new IllegalStateException("Cannot set a default bucket after a custom bucket has been created");
        }
        defaultBucket = true;
        return this;
    }

    public ItemBuilder<BucketItem, FluidBuilder<T, P>> bucket() {
        return bucket(BucketItem::new);
    }

    public <I extends BucketItem> ItemBuilder<I, FluidBuilder<T, P>> bucket(NonNullBiFunction<? extends BFlowingFluid, Item.Properties, ? extends I> factory) {
        if (this.defaultBucket == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
        }
        this.defaultBucket = false;
        NonNullSupplier<? extends BFlowingFluid> source = this.source;
        if (source == null) {
            throw new IllegalStateException("Cannot create a bucket before creating a source block");
        }
        return getOwner().<I, FluidBuilder<T, P>>item(this, bucketName, p -> ((NonNullBiFunction<BFlowingFluid, Item.Properties, ? extends I>) factory).apply(this.source.get(), p))
                .properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
                .model((ctx, prov) -> prov.generated(ctx, new ResourceLocation(getOwner().getModid(), "item/" + bucketName)));
    }

    protected abstract <I extends Item> void acceptItemModel(DataGenContext<Item, I> ctx, RegistrateItemModelProvider prov);

    @Beta
    public FluidBuilder<T, P> noBucket() {
        if (this.defaultBucket == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
        }
        this.defaultBucket = false;
        return this;
    }

    public abstract FluidBuilder<T, P> tag(TagKey<Fluid>... tags);

    @SafeVarargs
    public final FluidBuilder<T, P> removeTag(TagKey<Fluid>... tags) {
        this.tags.removeAll(Arrays.asList(tags));
        return this.removeTag(ProviderType.FLUID_TAGS, tags);
    }

    protected BFlowingFluid getSource() {
        NonNullSupplier<? extends BFlowingFluid> source = this.source;
        Preconditions.checkNotNull(source, "Fluid has no source block: " + sourceName);
        return source.get();
    }

    protected BFlowingFluid.Properties makeProperties() {
        NonNullSupplier<? extends BFlowingFluid> source = this.source;
        BFlowingFluid.Properties ret = new BFlowingFluid.Properties(source, asSupplier(), this.stillTexture, this.flowingTexture);
        this.properties.accept(ret);
        return ret;
    }

    @Override
    protected T createEntry() {
        return factory.apply(makeProperties());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public RegistryEntry<T> register() {
        if (defaultSource == Boolean.TRUE) {
            source(BFlowingFluid.Still::new);
        }
        if (defaultBlock == Boolean.TRUE) {
            block().register();
        }
        if (defaultBucket == Boolean.TRUE) {
            bucket().register();
        }
        NonNullSupplier<? extends BFlowingFluid> source = this.source;
        if (source != null) {
            getCallback().accept(sourceName, Registries.FLUID, (FluidBuilder) this, source);
        } else {
            throw new IllegalStateException("Fluid must have a source version: " + getName());
        }
        return super.register();
    }

    protected String makeDescriptionId(T fluid) {
        return Util.makeDescriptionId("fluid", new ResourceLocation(this.getOwner().getModid(), this.sourceName));
    }
}
