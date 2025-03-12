package net.electrisoma.bloodisfuel.infrastructure.utils;


import net.createmod.catnip.lang.Lang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Supplier;


@SuppressWarnings("unused")
public enum Mods {
    TIC("tconstruct"),
    BOP("biomesoplenty"),
    MC("minecraft"),
    CDG("createdieselgenerators"),
    BF("biofactory"),
    BM("biomancy");

    private final String id;

    Mods(String string) {
        id = Lang.asId(name());
    }

    public String id() {
        return id;
    }

    public ResourceLocation rl(String path) {
        return new ResourceLocation(id, path);
    }

    public Block getBlock(String id) {
        return ForgeRegistries.BLOCKS.getValue(rl(id));
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
