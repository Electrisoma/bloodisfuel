package net.electrisoma.bloodisfuel.infrastructure.events;

import net.electrisoma.bloodisfuel.registry.fluids.blocks.AbstractFluidBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide) return;

        BlockPos pos = entity.blockPosition();
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();

        ((AbstractFluidBlock) block).onEntityInFluid(level, pos, entity);
    }
}
