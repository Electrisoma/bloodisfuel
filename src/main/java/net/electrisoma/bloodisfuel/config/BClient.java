package net.electrisoma.bloodisfuel.config;

import net.createmod.catnip.config.ConfigBase;


@SuppressWarnings({"all"})
public class BClient extends ConfigBase {

    public final ConfigGroup client = group(0, "client",
            Comments.client);

    // custom fluids fog
    public final ConfigGroup fluidFogSettings =
            group(1, "fluidFogSettings",
                    Comments.fluidFogSettings);

    // fog transparency
    public final ConfigFloat fluidTransparencyMultiplier =
            f(1,.125f,128,"fluid",
                    Comments.fluidTransparencyMultiplier);

    public final ConfigGroup jeiSettings =
            group(1, "jeiSettings",
                    Comments.jeiSettings);

    public final ConfigBool flippedMobs =
            b(false,"flipped mobs",
                    Comments.flippedMobs);
    public final ConfigBool mouseTracking =
            b(true,"mouse tracking",
                    Comments.mouseTracking);
    public final ConfigFloat mobScale =
            f(1,0,1,"mob scale",
                    Comments.mobScale);


    @Override
    public String getName() {return "client";}

    private static class Comments {
        static String client =
                "Client-only settings - If you're looking for general settings, look inside your worlds serverconfig folder!";

        static String fluidFogSettings =
                "Configure your vision range when submerged in Create Blood is Fuel!'s custom fluids";
        static String jeiSettings =
                "Configure your jei settings in Create Blood is Fuel!'s custom jei categories";

        // fog transparency
        static String fluidTransparencyMultiplier =
                "The vision range through fluids will be multiplied by this factor";

        static String flippedMobs =
                "Whether mobs are rendered backwards or not in the jei category";
        static String mouseTracking =
                "Whether mobs track your mouse or not in the jei category";
        static String mobScale =
                "The scale of the mobs in the jei category";
    }
}