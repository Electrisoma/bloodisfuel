package net.electrisoma.bloodisfuel.registry.items.syringe_gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.electrisoma.bloodisfuel.BClient;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;


public class SyringeGunItemRenderer extends CustomRenderedItemModelRenderer {
    protected static final PartialModel VIAL = PartialModel.of(BloodIsFuel.asResource("item/syringe_gun/vial"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType,
                          PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        renderer.render(model.getOriginalModel(), light);
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        float angle = AnimationTickHolder.getRenderTime() * -2.5f;

        if (player != null) {
            boolean inMainHand = player.getMainHandItem() == stack;
            boolean inOffHand = player.getOffhandItem() == stack;

            if (inMainHand || inOffHand) {
                boolean leftHanded = player.getMainArm() == HumanoidArm.LEFT;
                float speed = BClient.SYRINGE_GUN_RENDER_HANDLER.getAnimation(inMainHand ^ leftHanded,
                        AnimationTickHolder.getPartialTicks());
                angle += 360 * Mth.clamp(speed * 5, 0, 1);
            }
        }

        angle %= 360;
        float offset = .5f / 16;

        ms.pushPose();
        ms.translate(0, offset, 0);
        ms.mulPose(Axis.XP.rotationDegrees(angle));
        ms.translate(0, -offset, 0);
        renderer.render(VIAL.get(), light);
        ms.popPose();
    }

}
