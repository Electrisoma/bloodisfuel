package net.electrisoma.bloodisfuel.registry.blocks.engine.portable_engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.electrisoma.bloodisfuel.registry.BPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;


@SuppressWarnings("unused")
public class PortableEngineBlockEntityRenderer extends ShaftRenderer<PortableEngineBlockEntity> {
    public PortableEngineBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(PortableEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel())) return;
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        BlockState engineState = be.getBlockState();

        PartialModel vialHorizontal = BPartialModels.ENGINE_VIAL_HORIZONTAL;
        PartialModel vialVertical = BPartialModels.ENGINE_VIAL_VERTICAL;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

//        if (engineState.getValue(FACING).getAxis().isHorizontal()) {
//            CachedBuffers.partial(vialHorizontal, engineState).center()
//                    .rotateYDegrees(engineState.getValue(FACING).toYRot())
//                    .uncenter()
//                    .light(light)
//                    .renderInto(ms, vb);
//        } else {
//            CachedBuffers.partial(vialVertical, engineState)
//                    .center().rotateYDegrees(engineState.getValue(FACING) ==
//                            Direction.DOWN ? 180 : 270)
//                    .rotateZ(engineState.getValue(FACING) ==
//                            Direction.DOWN ? 180 : 0)
//                    .uncenter()
//                    .light(light)
//                    .renderInto(ms, vb);
//        }
    }
}
