package net.electrisoma.bloodisfuel.base.data.recipe.compat;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.createmod.catnip.platform.CatnipServices;
import net.electrisoma.bloodisfuel.BloodIsFuel;

import net.electrisoma.bloodisfuel.base.data.recipe.BRecipeProvider;
import net.minecraft.data.CachedOutput;
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
public abstract class CompatRecipeGen extends BRecipeProvider {

    protected static final List<CompatRecipeGen> GENERATORS = new ArrayList<>();

    public static DataProvider registerAll(PackOutput pOutput) {

        //GENERATORS.add(new DistillationRecipeGen(pOutput));
        //GENERATORS.add(new LiquidBurningRecipeGen(output));


        return new DataProvider() {

            @Override
            public String getName() {
                return "Compat Recipes for " + BloodIsFuel.NAME;
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        };
    }

    public CompatRecipeGen(PackOutput generator) {
        super(generator);
    }

    protected abstract String getRecipeType();

    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = CatnipServices.REGISTRIES.getKeyOrThrow(item.get()
                    .asItem());
            return BloodIsFuel.asResource(registryName.getPath() + suffix);
        };
    }

    @Override
    public String getName() {
        return BloodIsFuel.NAME + " Compat Recipes"
                + getRecipeType();
    }
}