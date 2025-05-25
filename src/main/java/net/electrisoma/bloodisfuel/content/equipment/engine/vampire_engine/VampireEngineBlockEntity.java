package net.electrisoma.bloodisfuel.content.equipment.engine.vampire_engine;

import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.electrisoma.bloodisfuel.api.equipment.engine.abstract_engine.AbstractEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;


public class VampireEngineBlockEntity extends AbstractEngineBlockEntity {
    public ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

    public VampireEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        movementDirection = new ScrollOptionBehaviour<>(WindmillBearingBlockEntity.RotationDirection.class,
                CreateLang.translateDirect("contraptions.windmill.rotation_direction"), this, new VampireEngineValueBox());
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

        Direction facing = getBlockState().getValue(VampireEngineBlock.FACING);
        return convertToDirection(baseSpeed, facing);
    }
}
