package net.electrisoma.bloodisfuel.content.equipment.bloodextractor;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.electrisoma.bloodisfuel.registry.BBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class BloodExtractorBlock extends KineticBlock implements IBE<BloodExtractorBlockEntity>, ICogWheel {
    public BloodExtractorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public Class<BloodExtractorBlockEntity> getBlockEntityClass() {
        return BloodExtractorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BloodExtractorBlockEntity> getBlockEntityType() {
        return BBlockEntityTypes.BLOOD_EXTRACTOR.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}
