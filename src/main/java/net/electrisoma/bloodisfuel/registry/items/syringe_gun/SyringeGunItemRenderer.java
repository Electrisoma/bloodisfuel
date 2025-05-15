package net.electrisoma.bloodisfuel.registry.items.syringe_gun;

import net.electrisoma.bloodisfuel.BClient;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BRenderTypes;
import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidTypeManager;

import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;

import net.createmod.catnip.animation.AnimationTickHolder;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;


public class SyringeGunItemRenderer extends CustomRenderedItemModelRenderer implements SyringeUtils {

    protected static final PartialModel VIAL = PartialModel.of(BloodIsFuel.asResource("item/syringe_gun/vial"));
    protected static final PartialModel TRIGGER = PartialModel.of(BloodIsFuel.asResource("item/syringe_gun/trigger"));
    protected static final PartialModel ACCENTS = PartialModel.of(BloodIsFuel.asResource("item/syringe_gun/accents"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model,
                          PartialItemModelRenderer renderer, ItemDisplayContext transformType,
                          PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        renderer.render(model.getOriginalModel(), RenderType.solid(), light);
        renderer.render(TRIGGER.get(), RenderType.translucent(), light);
        renderer.render(ACCENTS.get(), RenderType.translucent(), light);

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

        FluidStack fluidStack = readFluid(stack);
        assert Minecraft.getInstance().level != null;
        RegistryAccess access = Minecraft.getInstance().level.registryAccess();
        SyringeFluidType fluidType = SyringeFluidTypeManager.fromFluid(fluidStack, access);

        boolean glowing = fluidType.isGlowing().orElse(false);
        RenderType vialRenderType = BRenderTypes.tintedTranslucent(glowing);

        ms.pushPose();
        ms.translate(0, offset, 0);
        ms.mulPose(Axis.XP.rotationDegrees(angle));
        ms.translate(0, -offset, 0);

        renderer.render(VIAL.get(), vialRenderType, light);
        ms.popPose();
    }
}
