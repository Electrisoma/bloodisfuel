package net.electrisoma.bloodisfuel.registry.items.syringe_gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;


public class SyringeGunRenderHandler extends ShootableGadgetRenderHandler {

    private float nextPitch;

    @Override
    protected void playSound(InteractionHand hand, Vec3 position) {
        SyringeProjectileEntity.playLaunchSound(Minecraft.getInstance().level, position, nextPitch);
    }

    @Override
    protected boolean appliesTo(ItemStack stack) {
        return stack.getItem() instanceof SyringeGunItem;
    }

    @Override
    protected void transformTool(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
        ms.translate(flip * -.1f, -.05, .14f);
        ms.scale(1, 1, 1);
        TransformStack.of(ms)
                .rotateXDegrees(recoil * 80);
    }

    @Override
    protected void transformHand(PoseStack ms, float flip, float equipProgress, float recoil, float pt) {
        ms.translate(flip * -.09, -.275, -.25);
        TransformStack.of(ms)
                .rotateZDegrees(flip * -10);
    }
}
