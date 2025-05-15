package net.electrisoma.bloodisfuel.registry;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;


@SuppressWarnings("DataFlowIssue")
public class BRenderTypes extends RenderStateShard {

    private static final RenderType TINTED_TRANSLUCENT =
            RenderType.create(bloodisfuelLayerName("tinted_translucent"),
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(CULL)
                    .createCompositeState(true)
    );

    public static final RenderType TINTED_GLOWING_TRANSLUCENT =
            RenderType.create(bloodisfuelLayerName("tinted_glowing_translucent"),
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(CULL)
                    .createCompositeState(true)
    );

    public static RenderType tintedTranslucent(boolean glowing) {
        return glowing ? TINTED_GLOWING_TRANSLUCENT : TINTED_TRANSLUCENT;
    }

    private static String bloodisfuelLayerName(String name) {
        return BloodIsFuel.MOD_ID + ":" + name;
    }

    // Protected fields :)
    private BRenderTypes() {
        super(null, null, null);
    }
}