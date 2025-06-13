package net.electrisoma.bloodisfuel.foundation.data.recipes;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.AllTags;
import com.simibubi.create.AllItems;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


@SuppressWarnings("unused")
public class BRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();
    static final int BUCKET = FluidType.BUCKET_VOLUME;
    static final int BOTTLE = 250;

    public BRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {}

    public static void registerAllProcessing(DataGenerator gen, PackOutput output) {
        GENERATORS.add(new BMixingRecipeGen(output));
        GENERATORS.add(new BFillingRecipeGen(output));
        GENERATORS.add(new BEmptyingRecipeGen(output));
        GENERATORS.add(new BCompactingRecipeGen(output));

        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return BloodIsFuel.NAME + "'s Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }
        });
    }

    protected static class I {
        static ItemLike leather() {
            return Items.LEATHER;
        }
        static ItemLike ironSword() {
            return Items.IRON_SWORD;
        }
        static ItemLike crossbow() {
            return Items.CROSSBOW;
        }
        static ItemLike bottle() {
            return Items.GLASS_BOTTLE;
        }
        static ItemLike tripwireHook() {
            return Items.TRIPWIRE_HOOK;
        }
        static ItemLike electronTube() {
            return AllItems.ELECTRON_TUBE;
        }
        static ItemLike precisionMechanism() {
            return AllItems.PRECISION_MECHANISM;
        }
        static TagKey<Item> brassNugget() {
            return AllTags.forgeItemTag("nuggets/brass");
        }
        static TagKey<Item> copperSheet() {
            return AllTags.forgeItemTag("plates/copper");
        }
        static TagKey<Item> brassSheet() {
            return AllTags.forgeItemTag("plates/brass");
        }
    }
}
