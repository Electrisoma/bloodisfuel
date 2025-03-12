package net.electrisoma.bloodisfuel.foundation.data.recipes;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.platform.CatnipServices;
import net.electrisoma.bloodisfuel.BloodIsFuel;
//import net.electrisoma.bloodisfuel.foundation.data.recipes.compat.DistillationRecipeGen;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;

import net.electrisoma.bloodisfuel.infrastructure.utils.Mods;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


@SuppressWarnings("unused")
public abstract class BProcessingRecipeGen extends BRecipeProvider {

    protected static final List<BProcessingRecipeGen> GENERATORS = new ArrayList<>();


    public static void registerAll(DataGenerator gen, PackOutput output) {

        GENERATORS.add(new MixingRecipeGen(output));
        GENERATORS.add(new EmptyingRecipeGen(output));
        GENERATORS.add(new CompactingRecipeGen(output));

        if(Mods.CDG.isLoaded())
            //GENERATORS.add(new DistillationRecipeGen(output));
        //if(ModList.get().isLoaded("createaddition"))
        //    GENERATORS.add(new LiquidBurningGen(output));


        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return BloodIsFuel.NAME + " Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        });
    }

    public BProcessingRecipeGen(PackOutput generator) {
        super(generator);
    }

    protected <T extends ProcessingRecipe<?>>
    GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform
                    .apply(new ProcessingRecipeBuilder<>(serializer.getFactory(),
                            new ResourceLocation(namespace, CatnipServices.REGISTRIES.getKeyOrThrow(itemLike.asItem())
                                    .getPath())).withItemIngredients(Ingredient.of(itemLike)))
                    .build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    <T extends ProcessingRecipe<?>>
    GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(BloodIsFuel.MOD_ID, singleIngredient, transform);
    }

    protected <T extends ProcessingRecipe<?>>
    GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), name.get()))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected <T extends ProcessingRecipe<?>>
    GeneratedRecipe create(ResourceLocation name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return createWithDeferredId(() -> name, transform);
    }

    <T extends ProcessingRecipe<?>>
    GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(BloodIsFuel.asResource(name), transform);
    }

    protected abstract IRecipeTypeInfo getRecipeType();

    protected <T extends ProcessingRecipe<?>>
    ProcessingRecipeSerializer<T> getSerializer() {
        return getRecipeType().getSerializer();
    }

    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = CatnipServices.REGISTRIES.getKeyOrThrow(item.get()
                    .asItem());
            return BloodIsFuel.asResource(registryName.getPath() + suffix);
        };
    }

    @Override
    public String getName() {
        return BloodIsFuel.NAME + " Processing Recipes"
                + getRecipeType()
                .getId()
                .getPath();
    }
}