package net.electrisoma.bloodisfuel.multiloader.forge;

import net.electrisoma.bloodisfuel.multiloader.Env;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.DistExecutor;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.Supplier;


public class EnvImpl {
    @Internal
    public static Env getCurrent() {
        return FMLEnvironment.dist ==
                Dist.CLIENT ?
                Env.CLIENT :
                Env.SERVER;
    }

    public static void executeOnClient(Supplier<Runnable> sup) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, sup);
    }
}