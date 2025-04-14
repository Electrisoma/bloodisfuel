package net.electrisoma.bloodisfuel.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public enum Env {

    CLIENT, SERVER;

    public static final Env CURRENT = getCurrent();

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (isCurrent())
            run.get().run();
    }

    public static <T> T unsafeRunForDist(Supplier<Supplier<T>> clientTarget, Supplier<Supplier<T>> serverTarget) {
        return switch (Env.CURRENT) {
            case CLIENT -> clientTarget.get().get();
            case SERVER -> serverTarget.get().get();
        };
    }

    @Internal
    @ExpectPlatform
    public static Env getCurrent() {
        throw new AssertionError();
    }

    @ExpectPlatform public static void executeOnClient(Supplier<Runnable> sup) {
        throw new AssertionError();
    }
}