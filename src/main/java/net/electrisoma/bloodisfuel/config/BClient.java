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

    @Override
    public String getName() {return "client";}

    private static class Comments {
        static String client =
                "Client-only settings - If you're looking for general settings, look inside your worlds serverconfig folder!";

        static String fluidFogSettings =
                "Configure your vision range when submerged in Create Blood is Fuel!'s custom fluids";

        // fog transparency
        static String fluidTransparencyMultiplier =
                "The vision range through fluids will be multiplied by this factor";
    }
}