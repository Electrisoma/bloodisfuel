package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.foundation.data.advancements.BAdvancement;

import net.minecraft.data.PackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Sets;


@SuppressWarnings("all")
public class BAdvancements implements DataProvider {

    public static final List<BAdvancement> ENTRIES = new ArrayList<BAdvancement>();

    public static void register() {
        BloodIsFuel.LOGGER.info("Registering advancements for " + BloodIsFuel.NAME);
    }

    public static final BAdvancement BASE = null,

    // Blood - Root

    ROOT = create("root", BItems.DRAINED_MEAT)
            .name("Blood is Fuel!")
            .description("Here Be Meat")
            .free()
            .build(),

    // Blood - Start

    VISCERA = create("viscera", BFluids.VISCERA.getBucket().get().getDefaultInstance().getItem())
            .name("Into the Fire")
            .description("The meat hath begun...")
            .after(ROOT)
            .onIconCollected()
            .build(),

    DRAINED_MEAT = create("drained_meat", Items.BEEF)
            .name("Poor fella")
            .description("Obtain the drained remains of a mob")
            .after(ROOT)
            .onItemCollected(BItems.DRAINED_MEAT)
            .build(),

    BLOOD = create("blood", BFluids.BLOOD.getBucket().get().getDefaultInstance().getItem())
            .name("The Meatgrinder")
            .description("Obtain a bucket of blood")
            .after(VISCERA)
            .onIconCollected()
            .build(),

    SYRINGE_BLADE = create("syringe_blade", BItems.SYRINGE_BLADE)
            .name("Let's practice medicine")
            .description("Obtain the Syringe Blade")
            .after(ROOT)
            .onIconCollected()
            .announce()
            .build(),

    // Blood - Enrichment

    ENRICHED_BLOOD = create("enriched_blood", BFluids.ENRICHED_BLOOD.getBucket().get().getDefaultInstance().getItem())
            .name("Double Down")
            .description("Obtain a bucket of enriched blood")
            .after(BLOOD)
            .onIconCollected()
            .build(),

    // Blood - Oil

    OIL_ENRICHED_BLOOD = create("oil_enriched_blood", BFluids.OIL_ENRICHED_BLOOD.getBucket().get().getDefaultInstance().getItem())
            .name("A One Machine Army")
            .description("Obtain a bucket of oil enriched blood")
            .after(ENRICHED_BLOOD)
            .onIconCollected()
            .announce()
            .build(),

    // Blood - Hidden

    BOILED = create("boiled", BFluids.BOILING_BLOOD.getBucket().get().getDefaultInstance().getItem())
            .name("Lobster Bath")
            .description("Boil to death in boiling blood")
            .after(ROOT)
            .secret()
            .challenge()
            .announce()
            .build()

//    FLAMING_BLOOD_THROWER = create("flaming_blood_thrower", b -> b
//            .icon(() -> BIF_Fluids.OIL_ENRICHED_BLOOD.getBucket().get().getDefaultInstance().getItem())
//            .title("A One Machine Army")
//            .description("Obtain a bucket of oil enriched blood")
//            .after(OIL_ENRICHED_BLOOD)
//            .whenItemCollected(BIF_Items.DRAINED_MEAT)
//            .special(SECRET)),
    ;

    private static BAdvancement.Builder create(String id, ItemLike icon) {
        return new BAdvancement.Builder(id, icon);
    }

    // Datagen
    private final PackOutput output;
    public BAdvancements(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {

        PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        List<CompletableFuture<?>> futures = new ArrayList<>();

        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            ResourceLocation id = advancement.getId();
            if (!set.add(id))
                throw new IllegalStateException("Duplicate advancement " + id);
            Path path = pathProvider.json(id);
            futures.add(DataProvider.saveStable(cache, advancement.deconstruct()
                    .serializeToJson(), path));
        };

        for (BAdvancement advancement : ENTRIES)
            advancement.save(consumer);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }


    public static void provideLang(BiConsumer<String, String> consumer) {
        for (BAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }

    public static ResourceLocation getBackground() {
        return BloodIsFuel.asResource("textures/gui/advancements.png");
    }

    @Override
    public String getName() {
        return BloodIsFuel.NAME + "'s Advancements";
    }
}