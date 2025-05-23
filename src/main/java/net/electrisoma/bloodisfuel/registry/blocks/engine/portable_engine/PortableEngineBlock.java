package net.electrisoma.bloodisfuel.registry.blocks.engine.portable_engine;

import net.electrisoma.bloodisfuel.registry.*;
import net.electrisoma.bloodisfuel.api.engine.abstract_engine.AbstractEngineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class PortableEngineBlock extends AbstractEngineBlock<PortableEngineBlockEntity> {
    public PortableEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<PortableEngineBlockEntity> getBlockEntityClass() {
        return PortableEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PortableEngineBlockEntity> getBlockEntityType() {
        return BBlockEntityTypes.PORTABLE_ENGINE.get();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case NORTH, SOUTH, EAST, WEST -> BShapes.ENGINE_HORIZONTAL.get(facing);
            case UP, DOWN -> BShapes.ENGINE_VERTICAL.get(facing);
        };
    }
}
