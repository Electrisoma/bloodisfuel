package net.electrisoma.bloodisfuel.content.equipment.syringe_blade;

import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidTypeManager;
import net.electrisoma.bloodisfuel.registry.BMenuTypes;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class SyringeFluidSelectMenu extends AbstractContainerMenu {
    private static final int ROWS = 3;
    private static final int COLUMNS = 9;
    private static final int TOTAL_SLOTS = ROWS * COLUMNS;

    private final ItemStack syringeItem;
    private final FluidStackContainer fluidContainer;

    private final List<FluidStack> allFluids = new ArrayList<>();

    public SyringeFluidSelectMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readItem(), readFluidList(buf));
    }
    public SyringeFluidSelectMenu(int id, Inventory inv, ItemStack syringeItem, List<FluidStack> allFluids) {
        super(BMenuTypes.SYRINGE_FLUID_SELECT.get(), id);
        this.syringeItem = syringeItem;
        this.allFluids.addAll(allFluids);
        this.fluidContainer = new FluidStackContainer(TOTAL_SLOTS);

        data.set(0, 0);
        addDataSlots(data);

        int yOffset = (ROWS - 4) * 18;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int index = row * COLUMNS + col;
                this.addSlot(new FluidDisplaySlot(index, 8 + col * 18, 18 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + yOffset));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 161 + yOffset));
        }

        fillFluidContainer();
    }

    private static List<FluidStack> readFluidList(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<FluidStack> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(FluidStack.readFromPacket(buf));
        }
        return list;
    }
    public static void writeFluidList(FriendlyByteBuf buf, List<FluidStack> fluids) {
        buf.writeVarInt(fluids.size());
        for (FluidStack fluid : fluids) {
            fluid.writeToPacket(buf);
        }
    }
    public List<FluidStack> getAllFluids() {
        return allFluids;
    }
    public FluidStack getFluid(int index) {
        int currentPage = data.get(0);
        int fluidIndex = currentPage * TOTAL_SLOTS + index;
        if (fluidIndex < 0 || fluidIndex >= allFluids.size()) return FluidStack.EMPTY;
        return allFluids.get(fluidIndex);
    }

    private final ContainerData data = new SimpleContainerData(1);
    public int getTotalSlots() {
        return TOTAL_SLOTS;
    }

    public void nextPage() {
        int maxPage = Math.max(1, (allFluids.size() + TOTAL_SLOTS - 1) / TOTAL_SLOTS);
        int currentPage = data.get(0);
        if (currentPage + 1 < maxPage) {
            data.set(0, currentPage + 1);
            fillFluidContainer();
            broadcastChanges();
        }
    }
    public void previousPage() {
        int currentPage = data.get(0);
        if (currentPage > 0) {
            data.set(0, currentPage - 1);
            fillFluidContainer();
            broadcastChanges();
        }
    }
    public int getPage() {
        return data.get(0);
    }

    @Override public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            previousPage();
            return true;
        } else if (id == 1) {
            nextPage();
            return true;
        }
        return false;
    }
    @Override public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        if (slotId >= 0 && slotId < this.slots.size()) {
            Slot slot = this.slots.get(slotId);
            if (slot instanceof FluidDisplaySlot fluidSlot) {
                int indexOnPage = fluidSlot.getSlotIndex();
                FluidStack fluid = fluidContainer.getFluid(indexOnPage);

                if (!fluid.isEmpty()) {
                    syringeItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
                        handler.drain(handler.getTankCapacity(0), IFluidHandler.FluidAction.EXECUTE);
                        FluidStack toFill = fluid.copy();
                        toFill.setAmount(handler.getTankCapacity(0));
                        handler.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                    });
                }
                return;
            }
        }

        super.clicked(slotId, dragType, clickType, player);
    }

    @Override public boolean stillValid(Player player) {
        return true;
    }
    @Override public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    private void fillFluidContainer() {
        fluidContainer.clear();

        int maxPage = Math.max(1, (allFluids.size() + TOTAL_SLOTS - 1) / TOTAL_SLOTS);
        int currentPage = data.get(0);

        if (currentPage >= maxPage) {
            currentPage = maxPage - 1;
            data.set(0, currentPage);
        }

        int start = currentPage * TOTAL_SLOTS;
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            int idx = start + i;
            if (idx < allFluids.size()) {
                fluidContainer.setFluid(i, allFluids.get(idx));
            } else {
                fluidContainer.setFluid(i, FluidStack.EMPTY);
            }
        }
    }
    private static final Set<String> IGNORED_POTIONS = Set.of("empty", "awkward", "water");
    public static List<FluidStack> buildFluidList(Player player) {
        RegistryAccess access = player.level().registryAccess();
        List<SyringeFluidType> types = SyringeFluidTypeManager.getAll(access);

        Map<String, FluidStack> mergedStacks = new TreeMap<>();

        for (SyringeFluidType type : types) {
            for (HolderSet<Fluid> fluidSet : type.fluids()) {
                if (fluidSet.stream().findAny().isEmpty()) continue;

                for (Holder<Fluid> fluidHolder : fluidSet) {
                    Fluid fluid = fluidHolder.value();
                    ResourceLocation fluidId = ForgeRegistries.FLUIDS.getKey(fluid);
                    if (fluidId == null) continue;
                    if (fluidId.getPath().contains("flowing")) continue;

                    if (type.isPotionType()) {
                        expandPotionFluids(fluid, mergedStacks);
                    } else {
                        FluidStack stack = new FluidStack(fluid, 1000);
                        String key = getNormalizedFluidKey(stack);
                        mergedStacks.putIfAbsent(key, stack);
                    }
                }
            }
        }

        return new ArrayList<>(mergedStacks.values());
    }
    private static void expandPotionFluids(Fluid fluid, Map<String, FluidStack> mergedStacks) {
        for (ResourceLocation potionId : ForgeRegistries.POTIONS.getKeys()) {
            if (IGNORED_POTIONS.contains(potionId.getPath())) continue;

            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);
            if (potion == null || potion.getEffects().isEmpty()) continue;

            FluidStack fs = new FluidStack(fluid, 1000);
            fs.getOrCreateTag().putString("Potion", potionId.toString());

            String key = getNormalizedFluidKey(fs);
            mergedStacks.putIfAbsent(key, fs);
        }
    }
    public static String getNormalizedFluidKey(FluidStack stack) {
        String baseName = stack.getDisplayName().getString().toLowerCase(Locale.ROOT).trim();

        if (stack.hasTag() && stack.getTag().contains("Potion")) {
            Potion potion = getPotion(stack);
            if (potion != null) {
                String effects = potion.getEffects().stream()
                        .map(e -> Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(e.getEffect())).toString())
                        .sorted()
                        .collect(Collectors.joining(","));
                return baseName + "<" + effects + ">";
            }
        }

        return baseName;
    }
    public static Potion getPotion(FluidStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Potion")) {
            return ForgeRegistries.POTIONS.getValue(ResourceLocation.tryParse(stack.getTag().getString("Potion")));
        }
        return null;
    }
    public class FluidDisplaySlot extends Slot {
        private final int fluidIndex;

        public int getSlotIndex() {
            return this.fluidIndex;
        }

        public FluidDisplaySlot(int fluidIndex, int x, int y) {
            super(new SimpleContainer(0), fluidIndex, x, y);
            this.fluidIndex = fluidIndex;
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean hasItem() {
            return !fluidContainer.getFluid(fluidIndex).isEmpty();
        }

        @Override
        public ItemStack getItem() {
            return ItemStack.EMPTY;
        }

        @Override
        public void set(ItemStack stack) {
        }
    }
    public static class FluidStackContainer {
        private final FluidStack[] fluids;

        public FluidStackContainer(int size) {
            this.fluids = new FluidStack[size];
            clear();
        }

        public void setFluid(int index, FluidStack stack) {
            fluids[index] = stack;
        }

        public FluidStack getFluid(int index) {
            return fluids[index];
        }

        public void clear() {
            Arrays.fill(fluids, FluidStack.EMPTY);
        }

        public int size() {
            return fluids.length;
        }
    }
}
