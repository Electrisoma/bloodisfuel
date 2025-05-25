package net.electrisoma.bloodisfuel.foundation.data.entries;

import com.simibubi.create.api.equipment.potatoCannon.PotatoCannonProjectileType;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.equipment.potatoCannon.AllPotatoProjectileEntityHitActions.PotionEffect;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BItems;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;

public class BPotatoCannonProjectiles {

    public static void bootstrap(BootstapContext<PotatoCannonProjectileType> ctx) {
        register(ctx, "drained_meat", new PotatoCannonProjectileType.Builder()
                .damage(5)
                .reloadTicks(15)
                .knockback(0.05f)
                .velocity(1.25f)
                .renderTumbling()
                .onEntityHit(new PotionEffect(MobEffects.POISON, 1, 160, true))
                .addItems(BItems.DRAINED_MEAT)
                .build()
        );
    }

    private static void register(BootstapContext<PotatoCannonProjectileType> ctx, String name, PotatoCannonProjectileType type) {
        ctx.register(ResourceKey.create(CreateRegistries.POTATO_PROJECTILE_TYPE, BloodIsFuel.asResource(name)), type);
    }
}
