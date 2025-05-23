package net.electrisoma.bloodisfuel.registry;

import com.simibubi.create.content.kinetics.base.ShaftVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.blocks.engine.portable_engine.PortableEngineBlockEntity;
import net.electrisoma.bloodisfuel.registry.blocks.engine.portable_engine.PortableEngineBlockEntityRenderer;

public class BBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering block entities for " + BloodIsFuel.NAME);
    }

    public static final BlockEntityEntry<PortableEngineBlockEntity> PORTABLE_ENGINE =
            REGISTRATE.blockEntity("portable_engine_tile_entity", PortableEngineBlockEntity::new)
            .visual(() -> ShaftVisual::new )
            .validBlocks(BBlocks.PORTABLE_ENGINE)
            .renderer(() -> PortableEngineBlockEntityRenderer::new)
            .register();
}
