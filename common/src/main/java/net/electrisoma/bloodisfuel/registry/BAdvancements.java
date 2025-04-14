package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.items.BItems;
import net.electrisoma.bloodisfuel.registry.advancement.BloodisFuelAdvancement;
import net.electrisoma.bloodisfuel.registry.advancement.BloodisFuelAdvancement.Builder;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.List;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;
import java.util.concurrent.CompletableFuture;

import static net.electrisoma.bloodisfuel.registry.advancement.BloodisFuelAdvancement.TaskType.SILENT;
import static net.electrisoma.bloodisfuel.registry.advancement.BloodisFuelAdvancement.TaskType.NORMAL;
import static net.electrisoma.bloodisfuel.registry.advancement.BloodisFuelAdvancement.TaskType.NOISY;
import static net.electrisoma.bloodisfuel.registry.advancement.BloodisFuelAdvancement.TaskType.SECRET;


@SuppressWarnings("unused")
public class BAdvancements implements DataProvider {

    public static void register() {
        // load the class and register everything
        BloodIsFuel.LOGGER.info("Registering advancements for " + BloodIsFuel.NAME);
    }

    public static final List<BloodisFuelAdvancement> ENTRIES = new ArrayList<>();
    public static final BloodisFuelAdvancement START = null,

    // Blood - Root
    ROOT = create("root", b -> b
            .icon(BItems.DRAINED_MEAT)
            .title("Blood is Fuel!")
            .description("Here Be Meat")
            .awardedForFree()
            .special(NORMAL)
    ),

    // Blood - Start
    VISCERA = create("viscera", b -> b
            .icon(() -> BFluids.VISCERA.get().getBucket())
            .title("Into the Fire")
            .description("The meat hath begun...")
            .after(ROOT)
            .whenIconCollected()
            .special(NORMAL)
    ),

    DRAINED_MEAT = create("drained_meat", b -> b
            .icon(Items.BEEF)
            .title("Poor fella")
            .description("Obtain the drained remains of a mob")
            .after(ROOT)
            .whenItemCollected(BItems.DRAINED_MEAT)
            .special(NORMAL)
    ),

    BLOOD = create("blood", b -> b
            .icon(() -> BFluids.BLOOD.get().getBucket())
            .title("The Meatgrinder")
            .description("Obtain a bucket of blood")
            .after(VISCERA)
            .whenIconCollected()
            .special(NORMAL)
    ),

    // probably some stuff for the syringe blade

    // Blood - Enrichment
    ENRICHED_BLOOD = create("enriched_blood", b -> b
            .icon(() -> BFluids.ENRICHED_BLOOD.get().getBucket())
            .title("Double Down")
            .description("Obtain a bucket of enriched blood")
            .after(BLOOD)
            .whenIconCollected()
            .special(NORMAL)
    ),

    // Blood - Oil
    OIL_ENRICHED_BLOOD = create("oil_enriched_blood", b -> b
            .icon(() -> BFluids.OIL_ENRICHED_BLOOD.get().getBucket())
            .title("A One Machine Army")
            .description("Obtain a bucket of oil enriched blood")
            .after(ENRICHED_BLOOD)
            .whenIconCollected()
            .special(NOISY)
    ),

    END = null;

    private static BloodisFuelAdvancement create(String id, UnaryOperator<Builder> b) {
        return new BloodisFuelAdvancement(id, b);
    }

    // Datagen

    private final PackOutput output;

    public BAdvancements(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
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

        for (BloodisFuelAdvancement advancement : ENTRIES)
            advancement.save(consumer);

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return BloodIsFuel.NAME + "'s Advancements";
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (BloodisFuelAdvancement advancement : ENTRIES)
            advancement.provideLang(consumer);
    }
}
