package net.electrisoma.bloodisfuel.infrastructure.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;


@SuppressWarnings("unused")
public class Utils {
    @ExpectPlatform
    public static Path configDir() {
        throw new AssertionError();
    }

    public static ResourceLocation location(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation location(String location) {
        return new ResourceLocation(location);
    }

    private Utils() {}
}
