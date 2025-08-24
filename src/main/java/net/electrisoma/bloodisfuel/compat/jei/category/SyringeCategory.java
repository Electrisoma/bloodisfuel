package net.electrisoma.bloodisfuel.compat.jei.category;

import mezz.jei.api.forge.ForgeTypes;
import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.compat.jei.SyringeInfo;

import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;


@SuppressWarnings({"removal", "RedundantSuppression"})
@ParametersAreNonnullByDefault
public class SyringeCategory implements IRecipeCategory<SyringeInfo> {
    private final IDrawable background;
    private final IDrawable icon;

    public SyringeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(120, 40);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, getSyringeIconItem());
    }

    @Override
    public RecipeType<SyringeInfo> getRecipeType() {
        return SyringeInfo.TYPE;
    }

    @Override public Component getTitle() {
        return Component.translatable("bloodisfuel.jei.syringe_fill");
    }
    @Override public IDrawable getBackground() {
        return background;
    }
    @Override public IDrawable getIcon() {
        return icon;
    }
    private ItemStack getSyringeIconItem() {
        Set<Item> syringes = Objects.requireNonNull(ForgeRegistries.ITEMS.tags())
                .getTag(net.minecraft.tags.TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(),
                        BloodIsFuel.path("syringes")))
                .stream()
                .collect(Collectors.toSet());

        return syringes.stream()
                .findFirst()
                .map(ItemStack::new)
                .orElse(new ItemStack(Items.GLASS_BOTTLE));
    }

    @Override public void setRecipe(IRecipeLayoutBuilder builder, SyringeInfo recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 13, 11)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(ForgeTypes.FLUID_STACK, recipe.fluidVariants());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 11)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStacks(recipe.outputVariants());
    }
    @Override public void draw(SyringeInfo recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(graphics, 40, 11);
    }
}
