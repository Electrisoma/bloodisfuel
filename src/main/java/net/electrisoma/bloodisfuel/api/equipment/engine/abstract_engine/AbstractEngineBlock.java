package net.electrisoma.bloodisfuel.api.equipment.engine.abstract_engine;

import net.electrisoma.bloodisfuel.api.utils.EngineBlockUtils;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;

import java.util.Objects;


@SuppressWarnings({"deprecation", "RedundantSuppression"})
public abstract class AbstractEngineBlock<T extends BlockEntity>
        extends DirectionalKineticBlock implements IBE<T>, ProperWaterloggedBlock, EngineBlockUtils {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty ENCHANTED = BooleanProperty.create("enchanted");

    public AbstractEngineBlock(Properties properties) {
        super(properties);
        registerDefaultState(super.defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        builder.add(FACING);
        builder.add(ENCHANTED);
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER);

        if ((context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) || preferred == null)
            return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(WATERLOGGED, waterlogged);

        return defaultBlockState().setValue(FACING, preferred).setValue(WATERLOGGED, waterlogged);
    }
    @Override public FluidState getFluidState(BlockState state) {
        return fluidState(state);
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        updateWater(level, state, currentPos);
        return state;
    }

    @Override public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide)
            return;

        withBlockEntityDo(level, pos, be -> {
            CompoundTag tag = stack.getTagElement("BlockEntityTag");
            if (tag != null) be.load(tag);
            if (be instanceof EngineNBTHandler handler) {
                handler.setCapacityEnchantLevel(stack.getEnchantmentLevel(AllEnchantments.CAPACITY.get()));
                if (stack.isEnchanted()) handler.setEnchantmentTag(stack.getEnchantmentTags());
                if (stack.hasCustomHoverName()) handler.setCustomName(stack.getHoverName());
            }
        });

        if (stack.getEnchantmentLevel(AllEnchantments.CAPACITY.get()) > 0)
            level.setBlock(pos, state.setValue(ENCHANTED, true), 2);
    }
    @Override public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(this.asItem());
        withBlockEntityDo(level, pos, be -> {
            CompoundTag tag = be.saveWithFullMetadata();
            stack.getOrCreateTag().put("BlockEntityTag", tag);
        });
        return stack;
    }

    @Override public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(FACING).getAxis() == face.getAxis();
    }
    @Override public Axis getRotationAxis(BlockState blockState) {
        return blockState.getValue(FACING).getAxis();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override public float getDefaultStressCapacity() {
        return 2048;
    }
    @Override public float getDefaultStressStressImpact() {
        return 0;
    }
    @Override public float getDefaultSpeed() {
        return 96;
    }

    public interface EngineNBTHandler {
        void setCapacityEnchantLevel(int level);
        void setEnchantmentTag(ListTag tag);
        void setCustomName(Component name);
    }
}
