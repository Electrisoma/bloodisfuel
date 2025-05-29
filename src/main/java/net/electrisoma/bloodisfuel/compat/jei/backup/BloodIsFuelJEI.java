//package net.electrisoma.bloodisfuel.compat.jei.backup;
//
//import com.simibubi.create.compat.jei.*;
//import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
//import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
//import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
//import com.simibubi.create.infrastructure.config.AllConfigs;
//import com.simibubi.create.infrastructure.config.CRecipes;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.JeiPlugin;
//import mezz.jei.api.gui.drawable.IDrawable;
//import mezz.jei.api.helpers.IGuiHelper;
//import mezz.jei.api.registration.IGuiHandlerRegistration;
//import mezz.jei.api.registration.IRecipeCategoryRegistration;
//import mezz.jei.api.registration.IRecipeRegistration;
//import net.createmod.catnip.config.ConfigBase;
//import net.electrisoma.bloodisfuel.BloodIsFuel;
//import net.minecraft.client.Minecraft;
//import net.minecraft.core.RegistryAccess;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.ItemLike;
//
//import javax.annotation.ParametersAreNonnullByDefault;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.function.Consumer;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.function.Supplier;
//
//import static com.simibubi.create.compat.jei.CreateJEI.*;
//
//
//@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unused"})
//@JeiPlugin
//@ParametersAreNonnullByDefault
//public class BloodIsFuelJEI implements IModPlugin {
//    private static final ResourceLocation ID = new ResourceLocation("jei_plugin");
//
//    @Override
//    public ResourceLocation getPluginUid() {
//        return ID;
//    }
//
//    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();
//    private void loadCategories() {
//        allCategories.clear();
//    }
//
//    private <T extends Recipe<?>> CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
//        return new CategoryBuilder<>(recipeClass);
//    }
//
//    @Override public void registerCategories(IRecipeCategoryRegistration registration) {
//        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
//        registration.addRecipeCategories(new SyringeCategory(helper));
//    }
//    @Override public void registerRecipes(IRecipeRegistration registration) {
//        assert Minecraft.getInstance().level != null;
//        RegistryAccess access = Minecraft.getInstance().level.registryAccess();
//        List<SyringeInfo> entries = SyringeJeiHelper.collectAndGroupSyringeRecipes(access);
//        registration.addRecipes(SyringeInfo.TYPE, entries);
//    }
//
//    @Override
//    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
//        registration.addGenericGuiContainerHandler(AbstractSimiContainerScreen.class, new SlotMover());
//    }
//
//    @SuppressWarnings({"unused", "unchecked", "UnusedReturnValue", "RedundantSuppression"})
//    private class CategoryBuilder<T extends Recipe<?>> {
//        private final Class<? extends T> recipeClass;
//        private Predicate<CRecipes> predicate = cRecipes -> true;
//
//        private IDrawable background;
//        private IDrawable icon;
//
//        private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
//        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();
//
//        public CategoryBuilder(Class<? extends T> recipeClass) {
//            this.recipeClass = recipeClass;
//        }
//
//        public CategoryBuilder<T> enableIf(Predicate<CRecipes> predicate) {
//            this.predicate = predicate;
//            return this;
//        }
//        public CategoryBuilder<T> enableWhen(Function<CRecipes, ConfigBase.ConfigBool> configValue) {
//            predicate = c -> configValue.apply(c).get();
//            return this;
//        }
//
//        public CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
//            recipeListConsumers.add(consumer);
//            return this;
//        }
//        public CategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
//            return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
//        }
//        public CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred) {
//            return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
//                if (pred.test(recipe)) {
//                    recipes.add((T) recipe);
//                }
//            }));
//        }
//        public CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred, Function<Recipe<?>, T> converter) {
//            return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
//                if (pred.test(recipe)) {
//                    recipes.add(converter.apply(recipe));
//                }
//            }));
//        }
//        public CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
//            return addTypedRecipes(recipeTypeEntry::getType);
//        }
//        public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
//            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
//        }
//        public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType, Function<Recipe<?>, T> converter) {
//            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> recipes.add(converter.apply(recipe)), recipeType.get()));
//        }
//        public CategoryBuilder<T> addTypedRecipesIf(Supplier<RecipeType<? extends T>> recipeType, Predicate<Recipe<?>> pred) {
//            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> {
//                if (pred.test(recipe)) {
//                    recipes.add(recipe);
//                }
//            }, recipeType.get()));
//        }
//        public CategoryBuilder<T> addTypedRecipesExcluding(Supplier<RecipeType<? extends T>> recipeType,
//                                                           Supplier<RecipeType<? extends T>> excluded) {
//            return addRecipeListConsumer(recipes -> {
//                List<Recipe<?>> excludedRecipes = getTypedRecipes(excluded.get());
//                CreateJEI.<T>consumeTypedRecipes(recipe -> {
//                    for (Recipe<?> excludedRecipe : excludedRecipes) {
//                        if (doInputsMatch(recipe, excludedRecipe)) {
//                            return;
//                        }
//                    }
//                    recipes.add(recipe);
//                }, recipeType.get());
//            });
//        }
//        public CategoryBuilder<T> removeRecipes(Supplier<RecipeType<? extends T>> recipeType) {
//            return addRecipeListConsumer(recipes -> {
//                List<Recipe<?>> excludedRecipes = getTypedRecipes(recipeType.get());
//                recipes.removeIf(recipe -> {
//                    for (Recipe<?> excludedRecipe : excludedRecipes)
//                        if (doInputsMatch(recipe, excludedRecipe) && doOutputsMatch(recipe, excludedRecipe))
//                            return true;
//                    return false;
//                });
//            });
//        }
//
//        public CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
//            catalysts.add(supplier);
//            return this;
//        }
//        public CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
//            return catalystStack(() -> new ItemStack(supplier.get()
//                    .asItem()));
//        }
//
//        public CategoryBuilder<T> icon(IDrawable icon) {
//            this.icon = icon;
//            return this;
//        }
//        public CategoryBuilder<T> itemIcon(ItemLike item) {
//            icon(new ItemIcon(() -> new ItemStack(item)));
//            return this;
//        }
//        public CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
//            icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
//            return this;
//        }
//
//        public CategoryBuilder<T> background(IDrawable background) {
//            this.background = background;
//            return this;
//        }
//        public CategoryBuilder<T> emptyBackground(int width, int height) {
//            background(new EmptyBackground(width, height));
//            return this;
//        }
//
//        public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
//            Supplier<List<T>> recipesSupplier;
//            if (predicate.test(AllConfigs.server().recipes)) {
//                recipesSupplier = () -> {
//                    List<T> recipes = new ArrayList<>();
//                    for (Consumer<List<T>> consumer : recipeListConsumers)
//                        consumer.accept(recipes);
//                    return recipes;
//                };
//            } else {
//                recipesSupplier = Collections::emptyList;
//            }
//
//            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
//                    new mezz.jei.api.recipe.RecipeType<>(BloodIsFuel.asResource(name), recipeClass),
//                    Component.translatable("bloodisfuel.recipe." + name), background, icon, recipesSupplier, catalysts);
//            CreateRecipeCategory<T> category = factory.create(info);
//            allCategories.add(category);
//            return category;
//        }
//    }
//}
