//package net.electrisoma.bloodisfuel.base.data.recipe.compat;
//
//import net.electrisoma.bloodisfuel.BloodIsFuel;
//import net.electrisoma.bloodisfuel.base.data.recipe.BRecipeProvider;
//
//import net.createmod.catnip.platform.CatnipServices;
//
//import net.minecraft.data.CachedOutput;
//import net.minecraft.data.DataProvider;
//import net.minecraft.data.PackOutput;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.ItemLike;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.function.Supplier;
//
//
//@SuppressWarnings("unused")
//public abstract class CompatRecipeGen extends BRecipeProvider {
//
//    protected static final List<CompatRecipeGen> GENERATORS = new ArrayList<>();
//
//    public static DataProvider registerAll(PackOutput pOutput) {
//
//        //GENERATORS.add(new DistillationRecipeGen(pOutput));
//        //GENERATORS.add(new LiquidBurningRecipeGen(output));
//
//
//        return new DataProvider() {
//
//            @Override
//            public @NotNull String getName() {
//                return "Compat Recipes for " + BloodIsFuel.NAME;
//            }
//
//            @Override
//            public @NotNull CompletableFuture<?> run(@NotNull CachedOutput dc) {
//                return CompletableFuture.allOf(GENERATORS.stream()
//                        .map(gen -> gen.run(dc))
//                        .toArray(CompletableFuture[]::new));
//            }
//        };
//    }
//
//    public CompatRecipeGen(PackOutput generator) {
//        super(generator);
//    }
//
//    protected abstract String getRecipeType();
//
//    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
//        return () -> {
//            ResourceLocation registryName = CatnipServices.REGISTRIES.getKeyOrThrow(item.get()
//                    .asItem());
//            return BloodIsFuel.asResource(registryName.getPath() + suffix);
//        };
//    }
//
//    @Override
//    public @NotNull String getName() {
//        return BloodIsFuel.NAME + " Compat Recipes"
//                + getRecipeType();
//    }
//}