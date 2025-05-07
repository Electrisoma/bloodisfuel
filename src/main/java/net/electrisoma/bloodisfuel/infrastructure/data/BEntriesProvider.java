package net.electrisoma.bloodisfuel.infrastructure.data;

import com.simibubi.create.api.registry.CreateRegistries;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.infrastructure.data.entries.BDamageTypes;
import net.electrisoma.bloodisfuel.infrastructure.data.entries.BPotatoCannonProjectiles;
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
            ;

    public BEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(BloodIsFuel.MOD_ID));
    }

    @Override
    public String getName() {
        return BloodIsFuel.NAME + "'s Generated Registry Entries";
    }
}
