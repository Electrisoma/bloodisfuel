package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;


public class BPartialModels {
    public static void register() {
        BloodIsFuel.LOGGER.info("Registering partial models for " + BloodIsFuel.NAME);
    }

    public static final PartialModel
            ENGINE_VIAL_HORIZONTAL = block("engine/vials/vial_horizontal"),
            //ENGINE_VIAL_HORIZONTAL_OPAQUE = block("engine/vials/vial_horizontal_opaque"),
            ENGINE_VIAL_VERTICAL = block("engine/vials/vial_vertical")
            //ENGINE_VIAL_VERTICAL_OPAQUE = block("engine/vials/vial_vertical_opaque")
            ;

    private static PartialModel block(String path) {
        return PartialModel.of(BloodIsFuel.path("block/" + path));
    }
}
