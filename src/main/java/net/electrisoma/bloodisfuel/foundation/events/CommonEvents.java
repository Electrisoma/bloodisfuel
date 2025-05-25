package net.electrisoma.bloodisfuel.foundation.events;

import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.foundation.data.RuntimeDataGenerator;
import com.simibubi.create.foundation.pack.DynamicPack;
import com.simibubi.create.foundation.pack.DynamicPackSource;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.registry.BRegistries;
import net.electrisoma.bloodisfuel.foundation.data.entries.BDamageTypes;
import net.electrisoma.bloodisfuel.infrastructure.data.BRegistriesImpl;
import net.electrisoma.bloodisfuel.infrastructure.data.SyringeFluidRuntimeDataGenerator;
import net.electrisoma.bloodisfuel.registry.BAdvancements;
import net.electrisoma.bloodisfuel.registry.BBlockEntityTypes;
import net.electrisoma.bloodisfuel.content.equipment.engine.portable_engine.PortableEngineSpoutBehavior;
import net.electrisoma.bloodisfuel.api.fluids.blocks.AbstractFluidBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.phys.AABB;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;


@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> BlockSpoutingBehaviour.BY_BLOCK_ENTITY
                .register(BBlockEntityTypes.PORTABLE_ENGINE.get(), new PortableEngineSpoutBehavior()));
    }

    @SubscribeEvent public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getSource().is(BDamageTypes.BOILING)) BAdvancements.BOILED.awardTo(player);
    }
    @SubscribeEvent public static void onLivingEntityTick(LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        applyFluidEffectsToEntity(level, entity);
    }
    @SubscribeEvent public static void onLevelTick(LevelTickEvent event) {
        Level level = event.level;
        if (event.phase != LevelTickEvent.Phase.END || level.isClientSide()) return;
        AABB worldBox = new AABB(
                level.getMinBuildHeight(), level.getMinBuildHeight(), level.getMinBuildHeight(),
                level.getMaxBuildHeight(), level.getMaxBuildHeight(), level.getMaxBuildHeight()
        );
        for (Entity entity : level.getEntities((Entity) null, worldBox, e -> true))
            applyFluidEffectsToEntity(level, entity);
    }

    private static void applyFluidEffectsToEntity(Level level, Entity entity) {
        AABB box = entity.getBoundingBox().deflate(0.001);
        BlockPos.betweenClosedStream(
                (int) Math.floor(box.minX), (int) Math.floor(box.minY), (int) Math.floor(box.minZ),
                (int) Math.floor(box.maxX), (int) Math.floor(box.maxY), (int) Math.floor(box.maxZ)
        ).map(BlockPos::immutable).forEach(pos -> {
            BlockState blockState = level.getBlockState(pos);
            if (blockState.getBlock() instanceof AbstractFluidBlock fluidBlock) {
                FluidState fluidState = level.getFluidState(pos);
                float height = fluidState.getHeight(level, pos);
                double fluidSurfaceY = pos.getY() + height;
                double entityFeetY = entity.getY();
                if (fluidSurfaceY > entityFeetY) fluidBlock.entityInsideProxy(level, pos, entity);
            }
        });
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {

            if (event.getPackType() == PackType.SERVER_DATA) {
                DynamicPack dynamicPack = new DynamicPack("bloodisfuel:dynamic_data",
                        PackType.SERVER_DATA);
                SyringeFluidRuntimeDataGenerator.insertIntoPack(dynamicPack);
                event.addRepositorySource(new DynamicPackSource("bloodisfuel:dynamic_data",
                        PackType.SERVER_DATA, Pack.Position.BOTTOM, dynamicPack));
            }
        }
    }
}