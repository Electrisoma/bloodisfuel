package net.electrisoma.bloodisfuel.registry;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.content.equipment.bloodextractor.BloodExtractorBlockEntity;
import net.electrisoma.bloodisfuel.content.equipment.engine.portable_engine.PortableEngineBlockEntity;
import net.electrisoma.bloodisfuel.content.equipment.engine.portable_engine.PortableEngineBlockEntityRenderer;
import net.electrisoma.bloodisfuel.content.equipment.engine.vampire_engine.VampireEngineBlockEntity;
import net.electrisoma.bloodisfuel.content.equipment.engine.vampire_engine.VampireEngineBlockEntityRenderer;

public class BBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    public static BlockEntityEntry<PortableEngineBlockEntity> PORTABLE_ENGINE;
    public static BlockEntityEntry<VampireEngineBlockEntity> VAMPIRE_ENGINE;
    public static BlockEntityEntry<BloodExtractorBlockEntity> BLOOD_EXTRACTOR;

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering block entities for " + BloodIsFuel.NAME);

        PORTABLE_ENGINE  = REGISTRATE
                .blockEntity("portable_engine_tile_entity", PortableEngineBlockEntity::new)
                .visual(() -> ShaftVisual::new )
                .validBlocks(BBlocks.PORTABLE_ENGINE)
                .renderer(() -> PortableEngineBlockEntityRenderer::new)
                .register();

        VAMPIRE_ENGINE = REGISTRATE
                .blockEntity("vampire_engine_tile_entity", VampireEngineBlockEntity::new)
                .visual(() -> ShaftVisual::new )
                .validBlocks(BBlocks.VAMPIRE_ENGINE)
                .renderer(() -> VampireEngineBlockEntityRenderer::new)
                .register();

        BLOOD_EXTRACTOR = REGISTRATE
                .blockEntity("blood_extractor_tile_entity", BloodExtractorBlockEntity::new)
                .visual(() -> SingleAxisRotatingVisual.of(AllPartialModels.MILLSTONE_COG), false)
                .validBlocks(BBlocks.BLOOD_EXTRACTOR)
                .register();
    }
}
