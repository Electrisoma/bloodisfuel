package net.electrisoma.bloodisfuel.infrastructure.utils;

import net.minecraft.resources.ResourceLocation;


@SuppressWarnings("unused")
public class Utils {

    public static ResourceLocation location(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation location(String location) {
        return new ResourceLocation(location);
    }

    private Utils() {}
}
