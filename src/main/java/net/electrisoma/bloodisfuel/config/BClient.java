package net.electrisoma.bloodisfuel.config;

import net.createmod.catnip.config.ConfigBase;


@SuppressWarnings({"all"})
public class BClient extends ConfigBase {

    public final ConfigGroup client = group(0, "client",
            Comments.client);

    // custom fluid fog
    public final ConfigGroup fluidFogSettings =
            group(1, "fluidFogSettings",
                    Comments.fluidFogSettings);

    // fog transparency
    public final ConfigFloat visceraTransparencyMultiplier =
            f(1,.125f,128,"viscera",
                    Comments.visceraTransparencyMultiplier);
    public final ConfigFloat bloodTransparencyMultiplier =
            f(1,.125f,128,"blood",
                    Comments.bloodTransparencyMultiplier);
    public final ConfigFloat enrichedBloodTransparencyMultiplier =
            f(1,.125f,128,"enriched_blood",
                    Comments.enrichedBloodTransparencyMultiplier);
    public final ConfigFloat oilEnrichedBloodTransparencyMultiplier =
            f(1,.125f,128,"oil_enriched_blood",
                    Comments.oilEnrichedBloodTransparencyMultiplier);
    public final ConfigFloat dieselInfusedBloodTransparencyMultiplier =
            f(1,.125f,128,"diesel_infused_blood",
                    Comments.dieselInfusedBloodTransparencyMultiplier);
    public final ConfigFloat gasolineInfusedBloodTransparencyMultiplier =
            f(1,.125f,128,"gasoline_infused_blood",
                    Comments.gasolineInfusedBloodTransparencyMultiplier);

    @Override
    public String getName() {return "client";}

    private static class Comments {
        static String client =
                "Client-only settings - If you're looking for general settings, look inside your worlds serverconfig folder!";

        static String fluidFogSettings =
                "Configure your vision range when submerged in Create Blood is Fuel!'s custom fluids";

        // fog transparency
        static String visceraTransparencyMultiplier =
                "The vision range through viscera will be multiplied by this factor";
        static String bloodTransparencyMultiplier =
                "The vision range through blood will be multiplied by this factor";
        static String enrichedBloodTransparencyMultiplier =
                "The vision range through enriched blood will be multiplied by this factor";
        static String oilEnrichedBloodTransparencyMultiplier =
                "The vision range through oil enriched blood will be multiplied by this factor";
        static String dieselInfusedBloodTransparencyMultiplier =
                "The vision range through diesel infused blood will be multiplied by this factor";
        static String gasolineInfusedBloodTransparencyMultiplier =
                "The vision range through gasoline infused blood will be multiplied by this factor";
    }

}
