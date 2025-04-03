package net.electrisoma.bloodisfuel.multiloader.fabric;

import net.electrisoma.bloodisfuel.multiloader.Env;

import com.tterrag.registrate.fabric.EnvExecutor;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.Supplier;


public class EnvImpl {
    @Internal
    public static Env getCurrent() {
        return FabricLoader.getInstance().getEnvironmentType() ==
                EnvType.CLIENT ?
                Env.CLIENT :
                Env.SERVER;
    }

    public static void executeOnClient(Supplier<Runnable> sup) {
        EnvExecutor.runWhenOn(EnvType.CLIENT, sup);
    }
}

