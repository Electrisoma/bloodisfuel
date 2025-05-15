package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BRenderTypes;
import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidTypeManager;

import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;

import net.minecraftforge.fluids.FluidStack;

import com.mojang.blaze3d.vertex.PoseStack;


public class SyringeBladeItemRenderer extends CustomRenderedItemModelRenderer implements SyringeUtils {

    protected static final PartialModel VIAL = PartialModel.of(BloodIsFuel.asResource("item/syringe_blade/vial"));
    protected static final PartialModel HANDLE_GUARD = PartialModel.of(BloodIsFuel.asResource("item/syringe_blade/handle_guard"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer,
                          ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        renderer.render(model.getOriginalModel(), RenderType.solid(), light);
        renderer.render(HANDLE_GUARD.get(), RenderType.translucent(), light);

        FluidStack fluidStack = readFluid(stack);
        assert Minecraft.getInstance().level != null;
        RegistryAccess access = Minecraft.getInstance().level.registryAccess();
        SyringeFluidType fluidType = SyringeFluidTypeManager.fromFluid(fluidStack, access);

        boolean glowing = fluidType.isGlowing().orElse(false);
        RenderType vialRenderType = BRenderTypes.tintedTranslucent(glowing);

        ms.pushPose();
        TransformStack.of(ms)
                .translate(0.0, 0.0, 0.0);

        renderer.render(VIAL.get(), vialRenderType, light);
        ms.popPose();
    }
}
