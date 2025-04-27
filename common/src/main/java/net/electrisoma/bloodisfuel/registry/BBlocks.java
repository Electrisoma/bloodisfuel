package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.foundation.data.CreateRegistrate;


@SuppressWarnings("unused")
public class BBlocks {

	private static final CreateRegistrate REGISTRATE = BloodIsFuel.registrate();

	public static void register() {
		// load the class and register everything
		BloodIsFuel.LOGGER.info("Registering blocks for " + BloodIsFuel.NAME);
	}

//	public static final BlockEntry<Block> EXAMPLE_BLOCK =
//			REGISTRATE.block("example_block", Block::new)
//			.initialProperties(SharedProperties::softMetal)
//			.properties(p -> p.mapColor(MapColor.COLOR_GRAY))
//			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
//			.properties(p -> p.strength(1.0F,3600000.0F)) // Unexplodable
//			.properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
//			.transform(pickaxeOnly())
//			.lang("Example Block")
//			.simpleItem()
//			.register();
}
