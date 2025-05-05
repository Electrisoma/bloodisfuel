package net.electrisoma.bloodisfuel.infrastructure.utils;

import com.tterrag.registrate.builders.Builder;

import net.minecraftforge.fml.ModList;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;


@SuppressWarnings("all")
public enum Mods {

    MC("minecraft"),

    TIC("tconstruct"),
    BOP("biomesoplenty"),
    CDG("createdieselgenerators"),
    BF("biofactory"),
    BM("biomancy")

    ;

    private final String id;

    private Mods(String id) {
        this(id, b -> {
        });
    }

    private Mods(String id, Consumer<Builder> props) {
        this.id = id;
    }

    public ResourceLocation rl(String path) {
        return new ResourceLocation(id, path);
    }

    public Block getBlock(String id) {
        return ForgeRegistries.BLOCKS.getValue(rl(id));
    }

    public String getId() {
        return id;
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(id);
    }

    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        if (isLoaded())
            return Optional.of(toRun.get().get());
        return Optional.empty();
    }

    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (isLoaded()) {
            toExecute.get().run();
        }
    }
}
