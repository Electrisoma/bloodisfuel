package net.electrisoma.bloodisfuel.infrastructure.data;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.foundation.data.entries.BDamageTypes;
import net.electrisoma.bloodisfuel.foundation.data.entries.BEngineFluidTypes;
import net.electrisoma.bloodisfuel.foundation.data.entries.BPotatoCannonProjectiles;
import net.electrisoma.bloodisfuel.foundation.data.entries.BSyringeFluidTypes;

import com.simibubi.create.api.registry.CreateRegistries;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;

import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;


public class BEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, BDamageTypes::bootstrap)
            .add(CreateRegistries.POTATO_PROJECTILE_TYPE, BPotatoCannonProjectiles::bootstrap)
            .add(BRegistries.SYRINGE_FLUIDS, BSyringeFluidTypes::bootstrap)
            .add(BRegistries.ENGINE_FLUIDS, BEngineFluidTypes::bootstrap)
    ;

    public BEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(BloodIsFuel.MOD_ID));
    }

    @Override
    public String getName() {
        return BloodIsFuel.NAME + "'s Generated Registry Entries";
    }
}
