package net.electrisoma.bloodisfuel.registry.items.syringe_gun;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;


@SuppressWarnings("unused")
public class SyringeProjectileRenderer extends EntityRenderer<SyringeProjectileEntity> {

    public SyringeProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(SyringeProjectileEntity entity, float yaw, float pt, PoseStack ms, MultiBufferSource buffer, int light) {
        ms.pushPose();

        ms.translate(0, entity.getBoundingBox().getYsize() / 2 - 1 / 8f, 0);

        //renderRandomItem(ms, buffer, light);

        ms.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SyringeProjectileEntity entity) {
        return null;
    }
}
