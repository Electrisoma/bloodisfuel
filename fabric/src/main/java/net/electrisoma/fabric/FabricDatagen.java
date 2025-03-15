//package net.electrisoma.fabric;
//
//import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
//import net.electrisoma.bloodisfuel.BloodIsFuel;
//
//import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
//import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Set;
//
//
//public class FabricDatagen implements DataGeneratorEntrypoint {
//    @Override
//    public void onInitializeDataGenerator(FabricDataGenerator gen) {
//        Path bResources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
//        ExistingFileHelper helper = new ExistingFileHelper(
//                Set.of(bResources), Set.of("create"), false, null, null
//        );
//        FabricDataGenerator.Pack pack = gen.createPack();
//        BloodIsFuel.registrate().setupDatagen(pack, helper);
//        BloodIsFuel.gatherData(pack);
//    }
//}