package net.electrisoma.bloodisfuel.fabric;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.compat.Mods;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;


public class BloodDataFabric implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        // Ensure that all mods are present if they are needed for data gen
        for (Mods mod : Mods.values())
            mod.assertForDataGen();

        Path bloodisfuelResources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
        // fixme re-enable the existing file helper when porting lib's ResourcePackLoader.createPackForMod is fixed
        ExistingFileHelper helper = new ExistingFileHelper(
                Set.of(bloodisfuelResources), Set.of("create"), false, null, null
        );

        FabricDataGenerator.Pack pack = gen.createPack();
        BloodIsFuel.registrate().setupDatagen(pack, helper);
        BloodIsFuel.gatherData(pack);
    }
}