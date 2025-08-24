package net.electrisoma.bloodisfuel.content.equipment.syringe_blade;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class SyringeFluidSelectScreen extends AbstractContainerScreen<SyringeFluidSelectMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");
    private final int containerRows;

    private Button prevButton;
    private Button nextButton;

    public SyringeFluidSelectScreen(SyringeFluidSelectMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, (Component.literal("Creative Syringe Fluid Selector")));
        this.containerRows = 3;
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        int buttonY = this.topPos + 18 + containerRows * 18 + 5;
        assert Minecraft.getInstance().player != null;

        prevButton = Button.builder(Component.literal("<"), btn -> Minecraft.getInstance()
                .player.connection.send(new ServerboundContainerButtonClickPacket(menu.containerId, 0)))
                .pos(this.leftPos + 8, buttonY).size(20, 20).build();

        nextButton = Button.builder(Component.literal(">"), btn -> Minecraft.getInstance()
                .player.connection.send(new ServerboundContainerButtonClickPacket(menu.containerId, 1)))
                .pos(this.leftPos + this.imageWidth - 28, buttonY).size(20, 20).build();

        this.addRenderableWidget(prevButton);
        this.addRenderableWidget(nextButton);
        updateButtons();
    }

    private void updateButtons() {
        prevButton.active = menu.getPage() > 0;
        int maxPage = Math.max(1, (menu.getAllFluids().size() + menu.getTotalSlots() - 1) / menu.getTotalSlots());
        nextButton.active = menu.getPage() < maxPage - 1;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        int chestHeight = containerRows * 18 + 17;
        int inventoryHeight = 96;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, chestHeight);
        guiGraphics.blit(TEXTURE, x, y + chestHeight, 0, 126, this.imageWidth, inventoryHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        for (Slot slot : this.menu.slots) {
            if (slot instanceof SyringeFluidSelectMenu.FluidDisplaySlot) {
                slot.set(ItemStack.EMPTY);
            }
        }

        updateButtons();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderFluidSlots(guiGraphics);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Slot hovered = this.getSlotUnderMouse();

        if (hovered instanceof SyringeFluidSelectMenu.FluidDisplaySlot fluidSlot) {
            FluidStack fluid = menu.getFluid(fluidSlot.getSlotIndex());
            if (!fluid.isEmpty()) {
                guiGraphics.renderTooltip(this.font, fluid.getDisplayName(), mouseX, mouseY);
            }
        } else {
            super.renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    private void renderFluidSlots(GuiGraphics guiGraphics) {
        for (Slot slot : this.menu.slots) {
            if (slot instanceof SyringeFluidSelectMenu.FluidDisplaySlot fluidSlot) {
                FluidStack fluid = menu.getFluid(fluidSlot.getSlotIndex());
                if (!fluid.isEmpty()) {
                    int x = this.leftPos + slot.x;
                    int y = this.topPos + slot.y;
                    renderFluid(guiGraphics, fluid, x, y);
                }
            }
        }
    }

    public void renderFluid(GuiGraphics guiGraphics, FluidStack fluidStack, int x, int y) {
        if (fluidStack.isEmpty()) return;

        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions clientExtension = IClientFluidTypeExtensions.of(fluid);
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(clientExtension.getStillTexture(fluidStack));
        int color = clientExtension.getTintColor(fluidStack);

        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red   = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8  & 0xFF) / 255.0F;
        float blue  = (color       & 0xFF) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.setShaderColor(red, green, blue, alpha);

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        float u0 = sprite.getU0();
        float v0 = sprite.getV0();
        float u1 = sprite.getU1();
        float v1 = sprite.getV1();

        buffer.vertex(x, y + 16, 0).uv(u0, v1).endVertex();
        buffer.vertex(x + 16, y + 16, 0).uv(u1, v1).endVertex();
        buffer.vertex(x + 16, y, 0).uv(u1, v0).endVertex();
        buffer.vertex(x, y, 0).uv(u0, v0).endVertex();

        Tesselator.getInstance().end();

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}
