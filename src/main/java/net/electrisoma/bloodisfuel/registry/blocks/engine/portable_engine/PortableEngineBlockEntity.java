package net.electrisoma.bloodisfuel.registry.blocks.engine.portable_engine;

import net.electrisoma.bloodisfuel.api.engine.abstract_engine.AbstractEngineBlockEntity;

import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;


public class PortableEngineBlockEntity extends AbstractEngineBlockEntity {
    public ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

    public PortableEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        movementDirection = new ScrollOptionBehaviour<>(WindmillBearingBlockEntity.RotationDirection.class,
                CreateLang.translateDirect("contraptions.windmill.rotation_direction"), this, new PortableEngineValueBox());
        movementDirection.withCallback($ -> updateGeneratedRotation());
        behaviours.add(movementDirection);
    }
    @Override public float getGeneratedSpeed() {
        if (!enabled())
            return 0;

        float baseSpeed = getFuelSpeed();
        if (baseSpeed == 0)
            return 0;

        if (movementDirection != null &&
                movementDirection.getValue() == WindmillBearingBlockEntity.RotationDirection.COUNTER_CLOCKWISE.ordinal()) {
            baseSpeed *= -1;
        }

        Direction facing = getBlockState().getValue(PortableEngineBlock.FACING);
        return convertToDirection(baseSpeed, facing);
    }
}
