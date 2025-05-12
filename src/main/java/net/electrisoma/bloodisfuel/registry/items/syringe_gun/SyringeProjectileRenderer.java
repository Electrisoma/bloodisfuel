package net.electrisoma.bloodisfuel.registry.items.syringe_gun;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;


public class SyringeProjectileRenderer extends EntityRenderer<SyringeProjectileEntity> {
    private final ItemRenderer itemRenderer;

    public SyringeProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer(); // Get the ItemRenderer
    }


    @Override
    public void render(SyringeProjectileEntity entity, float yaw, float pt, PoseStack ms, MultiBufferSource buffer, int light) {
        ms.pushPose();

        ms.translate(0, entity.getBoundingBox().getYsize() / 2 - 1 / 8f, 0);

        renderRandomItem(ms, buffer, light);

        ms.popPose();
    }

    private void renderRandomItem(PoseStack ms, MultiBufferSource buffer, int light) {
        ItemStack randomItem = new ItemStack(Items.DIAMOND);
        Level world = Minecraft.getInstance().level;
        LivingEntity entity = Minecraft.getInstance().player;

        BakedModel model = itemRenderer.getModel(randomItem, world, entity, 0);

        itemRenderer.render(randomItem, ItemDisplayContext.NONE, false, ms,
                buffer, light, OverlayTexture.NO_OVERLAY, model);
    }

    @Override
    public ResourceLocation getTextureLocation(SyringeProjectileEntity entity) {
        return null;
    }
}
