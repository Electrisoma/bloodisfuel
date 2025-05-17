package net.electrisoma.bloodisfuel.foundation.data.recipes;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.AllTags;
import com.simibubi.create.AllItems;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@SuppressWarnings("unused")
public class BRecipeProvider extends RecipeProvider {
    protected final List<GeneratedRecipe> all = new ArrayList<>();

    public BRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> p_200404_1_) {
        all.forEach(c -> c.register(p_200404_1_));
        BloodIsFuel.LOGGER.info("{} registered {} recipe{}", getName(), all.size(), all.size() == 1 ? "" : "s");
    }

    protected GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(Consumer<FinishedRecipe> consumer);
    }

    protected static class Marker {}

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
