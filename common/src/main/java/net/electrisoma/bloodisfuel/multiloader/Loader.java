package net.electrisoma.bloodisfuel.multiloader;

import java.util.function.Supplier;

import dev.architectury.injectables.annotations.ExpectPlatform;

import org.jetbrains.annotations.ApiStatus;


public enum Loader {
    FORGE, FABRIC;

    public static final Loader CURRENT = getCurrent();

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (isCurrent())
            run.get().run();
    }

    @ApiStatus.Internal
    @ExpectPlatform
    public static Loader getCurrent() {
        throw new AssertionError();
    }
}
