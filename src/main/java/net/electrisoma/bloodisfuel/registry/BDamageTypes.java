package net.electrisoma.bloodisfuel.registry;

import com.simibubi.create.foundation.damageTypes.DamageTypeBuilder;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;


public class BDamageTypes {

    public static final ResourceKey<DamageType>
            BOILING = key("boiling")
    ;

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, BloodIsFuel.asResource(name));
    }

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        new DamageTypeBuilder(BOILING).effects(DamageEffects.BURNING).register(ctx);
    }
}
