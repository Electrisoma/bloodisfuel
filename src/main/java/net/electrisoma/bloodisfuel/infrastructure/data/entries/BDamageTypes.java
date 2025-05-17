package net.electrisoma.bloodisfuel.infrastructure.data.entries;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.foundation.damageTypes.DamageTypeBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageEffects;


public class BDamageTypes {
    public static final ResourceKey<DamageType>
            BOILING = key("boiling"),
            SYRINGE_BLADE = key("syringe_blade"),
            SYRINGE_GUN = key("syringe_gun");

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, BloodIsFuel.asResource(name));
    }

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        new DamageTypeBuilder(BOILING).effects(DamageEffects.BURNING).register(ctx);
        new DamageTypeBuilder(SYRINGE_BLADE).register(ctx);
        new DamageTypeBuilder(SYRINGE_GUN).register(ctx);
    }
}
