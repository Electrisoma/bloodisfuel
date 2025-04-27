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

import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.fluid.FluidStack;
import net.electrisoma.bloodisfuel.base.utils.LazySupplier;
import net.electrisoma.bloodisfuel.multiloader.RegistryPlatform;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;

/**
 * Copy of {@link com.tterrag.registrate.builders.FluidBuilder} to work with multiloader fluid impl
 */
@SuppressWarnings("all")
public abstract class FluidBuilder<T 
        extends ArchitecturyFlowingFluid, P> 
        extends AbstractBuilder<Fluid, T, P, FluidBuilder<T, P>> {

    public static <P> FluidBuilder<ArchitecturyFlowingFluid.Flowing, P> 
    create(AbstractRegistrate<?> owner, P parent, 
           String name, BuilderCallback callback, 
           ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        return create(owner, parent, name, callback, stillTexture, flowingTexture, ArchitecturyFlowingFluid.Flowing::new);
    }

    public static <T extends ArchitecturyFlowingFluid, P> FluidBuilder<T, P> 
    create(AbstractRegistrate<?> owner, P parent, 
           String name, BuilderCallback callback, 
           ResourceLocation stillTexture, ResourceLocation flowingTexture,
           NonNullFunction<ArchitecturyFluidAttributes, T> factory) {
        return RegistryPlatform.createFluidBuilder(owner, parent, name, callback, stillTexture, flowingTexture, factory)
                .defaultLang().defaultSource().defaultBlock().defaultBucket();
    }

    protected final ResourceLocation stillTexture;
    protected final ResourceLocation flowingTexture;
    public final String sourceName;
    protected final String bucketName;
    protected final NonNullFunction<ArchitecturyFluidAttributes, T> factory;

    @Nullable
    private Boolean defaultSource, defaultBlock, defaultBucket;
    private NonNullConsumer<ArchitecturyFluidAttributes> properties;
    @Nullable
    private NonNullSupplier<? extends ArchitecturyFlowingFluid> source;
    protected List<TagKey<Fluid>> tags = new ArrayList<>();

    protected FluidBuilder(AbstractRegistrate<?> owner, P parent,
                           String name, BuilderCallback callback,
                           ResourceLocation stillTexture, ResourceLocation flowingTexture,
                           NonNullFunction<ArchitecturyFluidAttributes, T> factory) {
        super((AbstractRegistrate<?>) owner, parent, "flowing_" + name, callback,
                (ResourceKey<Registry<Fluid>>) BuiltInRegistries.FLUID.key());
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.sourceName = name;
        this.bucketName = name + "_bucket";
        this.factory = factory;

        String bucketName = this.bucketName;
        this.properties = ArchitecturyFluidAttributes::getBucketItem;
    }

    public FluidBuilder<T, P> properties(NonNullConsumer<ArchitecturyFluidAttributes> cons) {
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

    public FluidBuilder<T, P> source(NonNullFunction<ArchitecturyFluidAttributes, ? extends ArchitecturyFlowingFluid> factory) {
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

    public abstract BlockBuilder<ArchitecturyLiquidBlock, FluidBuilder<T, P>> block();

    public <B extends ArchitecturyLiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block(NonNullBiFunction<NonNullSupplier<? extends T>, BlockBehaviour.Properties, ? extends B> factory) {
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

    public <I extends BucketItem> ItemBuilder<I, FluidBuilder<T, P>> bucket(NonNullBiFunction<? extends ArchitecturyFlowingFluid, Item.Properties, ? extends I> factory) {
        if (this.defaultBucket == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
        }
        this.defaultBucket = false;
        NonNullSupplier<? extends ArchitecturyFlowingFluid> source = this.source;
        if (source == null) {
            throw new IllegalStateException("Cannot create a bucket before creating a source block");
        }
        return getOwner().<I, FluidBuilder<T, P>>item(this, bucketName, p -> ((NonNullBiFunction<ArchitecturyFlowingFluid, Item.Properties, ? extends I>) factory).apply(this.source.get(), p))
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

    protected Fluid getSource() {
        NonNullSupplier<? extends ArchitecturyFlowingFluid> source = this.source;
        Preconditions.checkNotNull(source, "Fluid has no source block: " + sourceName);
        return source.get();
    }

    protected ArchitecturyFluidAttributes makeProperties() {
        NonNullSupplier<? extends ArchitecturyFlowingFluid> source = this.source;
        ArchitecturyFluidAttributes ret = new ArchitecturyFluidAttributes() {
            public @org.jetbrains.annotations.Nullable String getTranslationKey(@org.jetbrains.annotations.Nullable FluidStack stack) {
                return "";
            }

            public Fluid getFlowingFluid() {
                return this.getFlowingFluid();
            }

            public Fluid getSourceFluid() {
                return this.getSourceFluid();
            }

            public boolean canConvertToSource() {
                return this.canConvertToSource();
            }

            public int getSlopeFindDistance(@org.jetbrains.annotations.Nullable LevelReader level) {
                return this.getSlopeFindDistance();
            }

            public int getDropOff(@org.jetbrains.annotations.Nullable LevelReader level) {
                return this.getDropOff();
            }

            public @org.jetbrains.annotations.Nullable Item getBucketItem() {
                return this.getBucketItem();
            }

            public int getTickDelay(@org.jetbrains.annotations.Nullable LevelReader level) {
                return this.getTickDelay();
            }

            public float getExplosionResistance() {
                return this.getExplosionResistance();
            }

            public @org.jetbrains.annotations.Nullable LiquidBlock getBlock() {
                return this.getBlock();
            }

            @Deprecated
            public ResourceLocation getSourceTexture(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getSourceTexture();
            }

            @Deprecated
            public ResourceLocation getFlowingTexture(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getFlowingTexture();
            }

            @Deprecated
            public int getColor(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getColor();
            }

            public int getLuminosity(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getLuminosity();
            }

            public int getDensity(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getDensity();
            }

            public int getTemperature(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getTemperature();
            }

            public int getViscosity(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getViscosity();
            }

            public boolean isLighterThanAir(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.isLighterThanAir();
            }

            public Rarity getRarity(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getRarity();
            }

            public @org.jetbrains.annotations.Nullable SoundEvent getFillSound(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getFillSound();
            }

            public @org.jetbrains.annotations.Nullable SoundEvent getEmptySound(@org.jetbrains.annotations.Nullable FluidStack stack, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos) {
                return this.getEmptySound();
            }
        };
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
            source(ArchitecturyFlowingFluid.Source::new);
        }
        if (defaultBlock == Boolean.TRUE) {
            block().register();
        }
        if (defaultBucket == Boolean.TRUE) {
            bucket().register();
        }
        NonNullSupplier<? extends ArchitecturyFlowingFluid> source = this.source;
        if (source != null) {
            getCallback().accept(sourceName, BuiltInRegistries.FLUID.key(), (FluidBuilder) this, source);
        } else {
            throw new IllegalStateException("Fluid must have a source version: " + getName());
        }
        return super.register();
    }

    protected String makeDescriptionId(T fluid) {
        return Util.makeDescriptionId("fluid", new ResourceLocation(this.getOwner().getModid(), this.sourceName));
    }

}