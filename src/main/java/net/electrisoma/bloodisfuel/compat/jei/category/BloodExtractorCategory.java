package net.electrisoma.bloodisfuel.compat.jei.category;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.electrisoma.bloodisfuel.compat.jei.BloodExtractorInfo;
import net.electrisoma.bloodisfuel.config.BConfigs;
import net.electrisoma.bloodisfuel.registry.BItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Quaternionf;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;


@SuppressWarnings({"removal", "RedundantSuppression"})
public class BloodExtractorCategory implements IRecipeCategory<BloodExtractorInfo> {
    private final IDrawable background;
    private final IDrawable icon;

    private final Map<String, Entity> entityCache = new ConcurrentHashMap<>();

    public BloodExtractorCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(200, 72);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, getBloodExtractorIconItem());
    }

    @Override
    public RecipeType<BloodExtractorInfo> getRecipeType() {
        return BloodExtractorInfo.TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("bloodisfuel.jei.fluid_extraction");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    private ItemStack getBloodExtractorIconItem() {
        return new ItemStack(BItems.BLOOD_BOTTLE);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BloodExtractorInfo recipe, IFocusGroup focusGroup) {
        recipe.matchingMobs().stream()
                .map(Holder::value)
                .map(this::getSpawnEggForEntity)
                .filter(stack -> !stack.isEmpty())
                .findFirst()
                .ifPresent(spawnEgg -> builder.addSlot(RecipeIngredientRole.INPUT, 13, 31)
                        .setBackground(getRenderedSlot(), -1, -1)
                        .addItemStack(spawnEgg));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 173, 31)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(ForgeTypes.FLUID_STACK, recipe.fluidOutputs());
    }

    private ItemStack getSpawnEggForEntity(EntityType<?> type) {
        if (type == EntityType.PLAYER) {
            ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);

            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                GameProfile profile = mc.player.getGameProfile();
                CompoundTag tag = new CompoundTag();
                tag.putString("SkullOwner", profile.getName());
                playerHead.getOrCreateTag().merge(tag);
            }

            return playerHead;
        }

        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item instanceof SpawnEggItem egg && egg.getType(null) == type) {
                return new ItemStack(egg);
            }
        }
        return ItemStack.EMPTY;
    }
    private void renderEntity(PoseStack poseStack, int x, int y, float scale, Entity entity, float mouseX, float mouseY) {
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

        float rotationX;
        float rotationY;
        float yaw = 0;

        if (BConfigs.client().mouseTracking.get()) {
            if (BConfigs.client().flippedMobs.get()) {
                rotationX = (float) Math.atan((-x + mouseX) / 40.0F);
                rotationY = (float) Math.atan((-y + mouseY) / 40.0F);
            } else {
                rotationX = (float) Math.atan((x - mouseX) / 40.0F);
                rotationY = (float) Math.atan((y - mouseY) / 40.0F);
                yaw = 180;
            }
        } else {
            rotationX = 25f / 40f;
            rotationY = 1.0f;
            yaw = 180;
        }

        poseStack.pushPose();
        poseStack.translate(x, y, 50);
        poseStack.scale(scale, scale, -scale);

        Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
        Quaternionf quaternion1 = Axis.XP.rotationDegrees(rotationY * 20.0F);
        quaternion.mul(quaternion1);
        poseStack.mulPose(quaternion);

        float bodyYaw = yaw + rotationX * 40.0F;

        entity.setYRot(bodyYaw);
        entity.setYHeadRot(bodyYaw);
        entity.setYBodyRot(bodyYaw);

        dispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

        RenderSystem.disableCull();

        dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, poseStack, buffer, 15728880);

        buffer.endBatch();

        RenderSystem.enableCull();

        dispatcher.setRenderShadow(true);
        poseStack.popPose();
    }
    private float calculateEntityScale(Entity entity) {
        var pose = entity.getPose();
        var dims = entity.getDimensions(pose);

        float width = dims.width;
        float height = dims.height;

        float maxSize = Math.max(width / 0.9f, height / 0.9f);
        if (maxSize < 0.1f) maxSize = 0.1f;

        float targetSize = 36 * BConfigs.client().mobScale.getF();
        float scale = targetSize / maxSize;

        scale = Math.max(scale, 0.3f);

//        System.out.printf("Entity: %s Pose: %s Width: %.2f Height: %.2f → Scale: %.2f%n",
//                entity.getType().getDescriptionId(), pose, width, height, scale);

        return scale;
    }

    @Override
    public void draw(BloodExtractorInfo recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(graphics, 80, 11);

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        recipe.matchingMobs().stream()
                .map(Holder::value)
                .map(type -> {
                    String cacheKey;
                    if (type == EntityType.PLAYER) {
                        cacheKey = "player";
                    } else {
                        long ticks = System.currentTimeMillis() / 1000L;
                        long skinCycleIndex = ticks % 8;
                        cacheKey = type + ":" + skinCycleIndex;
                    }

                    return entityCache.computeIfAbsent(cacheKey, key -> {
                        Entity entity;

                        if (type == EntityType.PLAYER) {
                            assert mc.player != null;
                            GameProfile profile = mc.player.getGameProfile();

                            RemotePlayer fakePlayer = new RemotePlayer(mc.level, profile);
                            fakePlayer.setPos(0, 0, 0);

                            fakePlayer.setItemSlot(EquipmentSlot.HEAD, mc.player.getItemBySlot(EquipmentSlot.HEAD).copy());
                            fakePlayer.setItemSlot(EquipmentSlot.CHEST, mc.player.getItemBySlot(EquipmentSlot.CHEST).copy());
                            fakePlayer.setItemSlot(EquipmentSlot.LEGS, mc.player.getItemBySlot(EquipmentSlot.LEGS).copy());
                            fakePlayer.setItemSlot(EquipmentSlot.FEET, mc.player.getItemBySlot(EquipmentSlot.FEET).copy());

                            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, mc.player.getItemInHand(InteractionHand.MAIN_HAND).copy());
                            fakePlayer.setItemInHand(InteractionHand.OFF_HAND, mc.player.getItemInHand(InteractionHand.OFF_HAND).copy());

                            entity = fakePlayer;
                        } else {
                            entity = type.create(mc.level);
                            if (entity != null) {
                                long ticks = System.currentTimeMillis() / 1000L;
                                long skinCycleIndex = ticks % 8;
                                entity.setUUID(new UUID(0L, type.hashCode() + skinCycleIndex));
                            }
                        }

                        return entity;
                    });
                })
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(entity -> {
                    if (entity instanceof RemotePlayer fakePlayer) {
                        assert mc.player != null;
                        fakePlayer.setItemSlot(EquipmentSlot.HEAD, mc.player.getItemBySlot(EquipmentSlot.HEAD).copy());
                        fakePlayer.setItemSlot(EquipmentSlot.CHEST, mc.player.getItemBySlot(EquipmentSlot.CHEST).copy());
                        fakePlayer.setItemSlot(EquipmentSlot.LEGS, mc.player.getItemBySlot(EquipmentSlot.LEGS).copy());
                        fakePlayer.setItemSlot(EquipmentSlot.FEET, mc.player.getItemBySlot(EquipmentSlot.FEET).copy());
                        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, mc.player.getItemInHand(InteractionHand.MAIN_HAND).copy());
                        fakePlayer.setItemInHand(InteractionHand.OFF_HAND, mc.player.getItemInHand(InteractionHand.OFF_HAND).copy());
                    }

                    float scale = calculateEntityScale(entity);
                    int mobX = 100;
                    int mobY = 59;
                    AllGuiTextures.JEI_LIGHT.render(graphics, 76, 59);
                    renderEntity(graphics.pose(), mobX, mobY, scale, entity, (float) mouseX, (float) mouseY);
                });
    }
}
