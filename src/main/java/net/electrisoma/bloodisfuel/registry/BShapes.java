package net.electrisoma.bloodisfuel.registry;

import com.simibubi.create.AllShapes;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BShapes extends AllShapes {
    public static final VoxelShaper
            ENGINE_HORIZONTAL = shape(2, 3, 0, 14, 13, 16)
            .add(0,0,0,16,3,16)
            .forDirectional(Direction.NORTH),
            ENGINE_VERTICAL = shape(3, 3, 0, 13, 13, 16)
            .forDirectional(Direction.NORTH);

    private static Builder shape(VoxelShape shape) {
        return new Builder(shape);
    }
    private static Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }
    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }
}
