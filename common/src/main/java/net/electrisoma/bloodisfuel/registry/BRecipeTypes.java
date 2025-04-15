//package net.electrisoma.bloodisfuel.registry;
//
//import net.electrisoma.bloodisfuel.BloodIsFuel;
//import net.electrisoma.bloodisfuel.content.kinetics.juicer.JuicingRecipe;
//import net.electrisoma.bloodisfuel.multiloader.RegistryPlatform;
//
//import net.createmod.catnip.lang.Lang;
//
//import com.tterrag.registrate.util.nullness.NonNullSupplier;
//
//import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
//import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
//import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.RecipeType;
//
//import com.google.gson.JsonObject;
//
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//import javax.annotation.Nullable;
//
//
//public enum BRecipeTypes implements IRecipeTypeInfo {
//
//    JUICING(JuicingRecipe::new)
//
//    ;
//
//    private final ResourceLocation id;
//    private final Supplier<RecipeSerializer<?>> serializerObject;
//    @Nullable
//    private final RecipeType<?> typeObject;
//    private final NonNullSupplier<RecipeType<?>> type;
//
//    BRecipeTypes(NonNullSupplier<RecipeSerializer<?>> serializerSupplier,
//                 NonNullSupplier<RecipeType<?>> typeSupplier, boolean registerType) {
//        String name = Lang.asId(name());
//        id = BloodIsFuel.asResource(name);
//        serializerObject = RegistryPlatform.registerRecipeSerializer(this.id, serializerSupplier);
//        if (registerType) {
//            typeObject = typeSupplier.get();
//            RegistryPlatform.registerRecipeType(this.id, typeSupplier);
//            type = typeSupplier;
//        } else {
//            typeObject = null;
//            type = typeSupplier;
//        }
//    }
//
//    BRecipeTypes(NonNullSupplier<RecipeSerializer<?>> serializerSupplier) {
//        String name = Lang.asId(name());
//        id = BloodIsFuel.asResource(name);
//        serializerObject = RegistryPlatform.registerRecipeSerializer(this.id, serializerSupplier);
//        typeObject = simpleType(id);
//        type = () -> typeObject;
//        RegistryPlatform.registerRecipeType(this.id, this.type);
//    }
//
//    BRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
//        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
//    }
//
//    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
//        String stringId = id.toString();
//        return new RecipeType<>() {
//            @Override
//            public String toString() {
//                return stringId;
//            }
//        };
//    }
//
//    @Override public ResourceLocation getId() {
//        return this.id;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T extends RecipeSerializer<?>> T getSerializer() {
//        return (T) this.serializerObject.get();
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T extends RecipeType<?>> T getType() {
//        return (T) this.type.get();
//    }
//
//    public static void register() {}
//
//    private static <T extends Recipe<?>> NonNullSupplier<RecipeSerializer<?>>
//    noSerializer(Function<ResourceLocation, T> prov) {
//        return () -> new SimpleRecipeSerializer<>(prov);
//    }
//
//    private static class SimpleRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
//
//        private final Function<ResourceLocation, T> constructor;
//
//        public SimpleRecipeSerializer(Function<ResourceLocation, T> constructor) {
//            this.constructor = constructor;
//        }
//
//        @Override
//        public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
//            return this.constructor.apply(recipeId);
//        }
//
//        @Override
//        public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
//            return this.constructor.apply(recipeId);
//        }
//
//        @Override
//        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
//        }
//    }
//
//}
