//package net.electrisoma.bloodisfuel.registry.items.syringe_blade;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import dev.engine_room.flywheel.lib.transform.TransformStack;
//import net.electrisoma.bloodisfuel.registry.BItems;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.player.AbstractClientPlayer;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraft.client.renderer.ItemInHandRenderer;
//import net.minecraft.client.renderer.entity.player.PlayerRenderer;
//import net.minecraft.util.Mth;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.HumanoidArm;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.ItemDisplayContext;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.RenderHandEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
//
//
//@EventBusSubscriber(value = Dist.CLIENT)
//public class SyringeBladeRenderHandler {
//
//    public static float mainHandAnimation = 0f;
//    public static float lastMainHandAnimation = 0f;
//
//    public static void tick() {
//        lastMainHandAnimation = mainHandAnimation;
//
//        Minecraft mc = Minecraft.getInstance();
//        LocalPlayer player = mc.player;
//        if (player == null) return;
//
//        mainHandAnimation *= Mth.clamp(mainHandAnimation, 0.8f, 0.99f);
//
//        if (!BItems.SYRINGE_BLADE.isIn(getRenderedOffHandStack())) return;
//        ItemStack main = getRenderedMainHandStack();
//        if (main.isEmpty() || !(main.getItem() instanceof BlockItem)) return;
//        if (!Minecraft.getInstance().getItemRenderer().getModel(main, null, null, 0).isGui3d()) return;
//    }
//
//    @SubscribeEvent
//    public static void onRenderPlayerHand(RenderHandEvent event) {
//        Minecraft mc = Minecraft.getInstance();
//        LocalPlayer player = mc.player;
//        if (player == null) return;
//
//        ItemStack heldItem = event.getItemStack();
//        if (!BItems.SYRINGE_BLADE.isIn(heldItem)) return;
//
//        InteractionHand hand = event.getHand();
//        boolean rightHand = hand == InteractionHand.MAIN_HAND ^ player.getMainArm() == HumanoidArm.LEFT;
//
//        PoseStack ms = event.getPoseStack();
//        var msr = TransformStack.of(ms);
//
//        AbstractClientPlayer abstractclientplayerentity = mc.player;
//        RenderSystem.setShaderTexture(0, abstractclientplayerentity.getSkinTextureLocation());
//
//        float flip = rightHand ? 1.0F : -1.0F;
//        float swingProgress = event.getSwingProgress();
//        boolean blockItem = heldItem.getItem() instanceof BlockItem;
//        float equipProgress = blockItem ? 0 : event.getEquipProgress() / 4;
//
//        if (swingProgress > 0.01f) mainHandAnimation = Mth.lerp(0.2f, mainHandAnimation, 1.0f);
//        else mainHandAnimation *= 0.6f;
//
//        float swing = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
//        float animation = swing * 0.3f;
//
//        ms.pushPose();
//
//        // player hand
//        ms.translate(
//                flip * (0.64000005F - 0.1f),
//                -0.4F + equipProgress * -0.6F,
//                -0.9F + 0.3f - animation
//        );
//
//        ms.pushPose();
//        msr.rotateYDegrees(flip * 74.0F);
//        ms.translate(flip * -1.0F, 3.6F, 3.5F);
//
//        msr.rotateZDegrees(flip * 120)
//                .rotateXDegrees(200)
//                .rotateYDegrees(flip * -135.0F);
//
//        ms.translate(flip * 5.6F, 0.0F, 0.0F);
//        msr.rotateYDegrees(flip * 40.0F);
//        ms.translate(flip * 0.05f, -0.3f, -0.3f);
//
//        PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
//        if (rightHand) playerRenderer.renderRightHand(ms, event.getMultiBufferSource(), event.getPackedLight(), player);
//        else playerRenderer.renderLeftHand(ms, event.getMultiBufferSource(), event.getPackedLight(), player);
//
//        ms.popPose();
//
//        // syringe
//        ms.pushPose();
//        ms.translate(
//                flip * -0.1f,
//                -0.05,
//                -0.25f
//        );
//
//        ItemInHandRenderer itemRenderer = mc.getEntityRenderDispatcher().getItemInHandRenderer();
//        ItemDisplayContext context = rightHand ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
//        itemRenderer.renderItem(player, heldItem, context, !rightHand, ms, event.getMultiBufferSource(), event.getPackedLight());
//        ms.popPose();
//
//        ms.popPose();
//        event.setCanceled(true);
//    }
//
//    private static ItemStack getRenderedMainHandStack() {
//        return Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().mainHandItem;
//    }
//    private static ItemStack getRenderedOffHandStack() {
//        return Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().offHandItem;
//    }
//}
//
