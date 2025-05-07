package net.electrisoma.bloodisfuel.registry.items.syringe_blade;

import net.electrisoma.bloodisfuel.registry.BEnchantments;
import net.electrisoma.bloodisfuel.registry.BTags;
import net.electrisoma.bloodisfuel.registry.BFluids;
import net.electrisoma.bloodisfuel.registry.enchantments.ChargesEnchantment;
import net.electrisoma.bloodisfuel.registry.items.ItemUtils;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.content.equipment.armor.CapacityEnchantment;

import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class SyringeBladeItem extends SwordItem
        implements CustomArmPoseItem, CapacityEnchantment.ICapacityEnchantable, ChargesEnchantment.ICapacityEnchantable, ItemUtils {

    private boolean isOnCooldown;
    private boolean offHandPower;

    public SyringeBladeItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    // we cant just have the item not have a cooldown or anything, that would be unbalanced
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (BTags.BItemTags.SYRINGE_BLADE.matches(stack) && entity instanceof Player player && isSelected) {
            isOnCooldown = player.getCooldowns().isOnCooldown(stack.getItem());
            offHandPower = BTags.BItemTags.SYRINGE_BLADE.matches(player.getOffhandItem().getItem());
        }
    }

    // what happens when the player attacks mobs
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return false;

        FluidStack fluidStack = readFluid(stack);
        SyringeFluidTypeManager fluidType = SyringeFluidTypeManager.fromFluid(fluidStack);

        int capacity = getCapacity(stack);
        int charges = getChargeCount(stack);
        int useAmount = getUseAmount(capacity, charges);

        fluidStack.shrink(useAmount);
        writeFluid(stack, fluidStack);

        // grab blood if empty
        if (fluidStack.isEmpty()) {
            FluidStack newBlood = new FluidStack(BFluids.BLOOD.get(), capacity);
            writeFluid(stack, newBlood);
            target.hurt(player.damageSources().playerAttack(player), 2.0F);
            return true;
        }

        // if not empty, use what it has to hurt the enemy
        if (fluidStack.getAmount() < useAmount) {
            stack.getOrCreateTag().remove("Fluid");
            target.hurt(player.damageSources().playerAttack(player), 2.0F);
            return true;
        }

        // effects
        List<MobEffectInstance> effects = fluidType.getEffects(fluidStack);
        for (MobEffectInstance effect : effects) {
            target.addEffect(new MobEffectInstance(effect));
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    // attributes and stuff
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
                if (entry.getKey() != Attributes.ATTACK_DAMAGE) {
                    builder.put(entry.getKey(), entry.getValue());
                }
            }

            int currentAmount = getCurrentFillLevel(stack);
            int capacity = getCapacity(stack);
            int charges = getChargeCount(stack);
            int useAmount = getUseAmount(capacity, charges);

            boolean isDepleted = currentAmount < useAmount;
            double baseDamage = isDepleted ? 1.5 : 6.0;

            builder.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                            baseDamage, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }

        return modifiers;
    }

    // helper method to assist with the charges
    public static int getUseAmount(int capacity, int charges) {
        return (int) Math.ceil((double) capacity / charges);
    }

    // tooltip stuff, like the fluid counter
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
        tooltipMaker(tooltip, stack);
    }

    // bar color stuff based on fluid
    @Override
    public int getBarColor(ItemStack stack) {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("Fluid"));
        SyringeFluidTypeManager fluidType = SyringeFluidTypeManager.fromFluid(fluidStack);

        return fluidType.getColor(fluidStack);
    }

    // bar visibility based on the presence of fluid
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getCurrentFillLevel(stack) > 0;
    }

    // bar progress based on fluid amount
    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13 * (getCurrentFillLevel(stack) / (float) getCapacity(stack)));
    }

    // valid enchantments
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

         if (enchantment == AllEnchantments.CAPACITY.get()) return true;
         if (enchantment == BEnchantments.BLADE_CHARGES.get()) return true;
         if (enchantment == Enchantments.SHARPNESS) return true;
         if (enchantment == Enchantments.FIRE_ASPECT) return true;

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    // is it enchantable? :shrug:
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    // lets it be used as a fluid container
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return getFluidHandler(stack);
    }

    // arm pose stuff, i want to mess with this later
    // currently not doing anything
    @Override
    @Nullable
    public ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        if (!player.swinging) return ArmPose.ITEM;
        return null;
    }
}