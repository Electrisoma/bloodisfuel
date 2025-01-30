package net.electrisoma.bloodisfuel.infrastructure.utils;

import net.electrisoma.bloodisfuel.infrastructure.Lang;

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

    Mods(String mod) {
        this.id = Lang.asId(name());
    }

    public String id() {
        return this.id; }

    public ResourceLocation resource(String path) {
        return Utils.location(id, path); }
    public Block getBlock(String id) {
        return ForgeRegistries.BLOCKS.getValue(this.resource(id)); }
    public boolean isLoaded() {
        return ModList.get().isLoaded(this.id); }

    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        return this.isLoaded() ? Optional.of(toRun.get().get()) : Optional.empty();
    }

    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (isLoaded()) toExecute.get().run();
    }
}
