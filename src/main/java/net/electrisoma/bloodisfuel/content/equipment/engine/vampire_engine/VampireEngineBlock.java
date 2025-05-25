package net.electrisoma.bloodisfuel.content.equipment.engine.vampire_engine;

import net.electrisoma.bloodisfuel.api.equipment.engine.abstract_engine.AbstractEngineBlock;
import net.electrisoma.bloodisfuel.registry.BBlockEntityTypes;
import net.electrisoma.bloodisfuel.registry.BShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


@SuppressWarnings("deprecation")
public class VampireEngineBlock extends AbstractEngineBlock<VampireEngineBlockEntity> {
    public VampireEngineBlock(Properties properties) {
        super(properties);
    }

    @Override public Class<VampireEngineBlockEntity> getBlockEntityClass() {
        return VampireEngineBlockEntity.class;
    }
    @Override public BlockEntityType<? extends VampireEngineBlockEntity> getBlockEntityType() {
        return BBlockEntityTypes.VAMPIRE_ENGINE.get();
    }

    @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case NORTH, SOUTH, EAST, WEST -> BShapes.ENGINE_HORIZONTAL.get(facing);
            case UP, DOWN -> BShapes.ENGINE_VERTICAL.get(facing);
        };
    }
}
