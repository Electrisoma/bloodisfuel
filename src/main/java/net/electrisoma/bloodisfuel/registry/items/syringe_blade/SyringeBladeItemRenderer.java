package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SyringeBladeItemRenderer extends CustomRenderedItemModelRenderer {
    protected static final PartialModel VIAL = PartialModel.of(BloodIsFuel.asResource("item/syringe_blade/vial"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType,
        PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        var stacker = TransformStack.of(ms);
        float animation = 0.25f;
        boolean leftHand = transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        boolean rightHand = transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;

//        if (leftHand || rightHand)
//            animation = Mth.lerp(AnimationTickHolder.getPartialTicks(),
//                    SyringeBladeRenderHandler.lastMainHandAnimation,
//                    SyringeBladeRenderHandler.mainHandAnimation);

        animation = animation * animation * animation;

        // syringe vial
        renderer.render(model.getOriginalModel(), light);
        ms.pushPose();

        renderer.render(VIAL.get(), light);
        ms.popPose();
    }
}
