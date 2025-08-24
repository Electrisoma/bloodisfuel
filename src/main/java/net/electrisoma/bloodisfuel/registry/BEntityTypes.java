package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.content.equipment.syringe_gun.SyringeProjectileEntity;
import net.electrisoma.bloodisfuel.content.equipment.syringe_gun.SyringeProjectileRenderer;

import com.simibubi.create.foundation.data.CreateEntityBuilder;

import net.createmod.catnip.lang.Lang;

import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;


// TODO: add mobs and possibly add extra rendering to the syringe projectile
public class BEntityTypes {
    public static EntityEntry<SyringeProjectileEntity> SYRINGE_PROJECTILE;

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering entity types for " + BloodIsFuel.NAME);

        SYRINGE_PROJECTILE = register(
                "syringe_projectile", SyringeProjectileEntity::new, () -> SyringeProjectileRenderer::new,
                MobCategory.MISC, 4, 20,
                true, false,
                SyringeProjectileEntity::build
        ).register();
    }

    private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory,
                                                                         NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
                                                                         MobCategory group, int range, int updateFrequency, boolean sendVelocity, boolean immuneToFire,
                                                                         NonNullConsumer<EntityType.Builder<T>> propertyBuilder) {
        String id = Lang.asId(name);
        return (CreateEntityBuilder<T, ?>) BloodIsFuel.registrate()
                .entity(id, factory, group)
                .properties(b -> b.setTrackingRange(range)
                        .setUpdateInterval(updateFrequency)
                        .setShouldReceiveVelocityUpdates(sendVelocity))
                .properties(propertyBuilder)
                .properties(b -> {
                    if (immuneToFire)
                        b.fireImmune();
                }).renderer(renderer);
    }
}
