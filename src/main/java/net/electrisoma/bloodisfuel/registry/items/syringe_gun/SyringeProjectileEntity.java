package net.electrisoma.bloodisfuel.registry.items.syringe_gun;

import com.simibubi.create.AllSoundEvents;
import net.electrisoma.bloodisfuel.api.utils.FluidUtils;
import net.electrisoma.bloodisfuel.api.utils.ItemCapacityUtils;
import net.electrisoma.bloodisfuel.api.utils.SyringeUtils;
import net.electrisoma.bloodisfuel.api.utils.TooltipUtils;
import net.electrisoma.bloodisfuel.registry.BEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkHooks;

import java.util.Objects;

public class SyringeProjectileEntity extends AbstractHurtingProjectile
        implements IEntityAdditionalSpawnData, FluidUtils, TooltipUtils, SyringeUtils, ItemCapacityUtils {

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    public SyringeProjectileEntity(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    private FluidStack fluid = FluidStack.EMPTY;

    public SyringeProjectileEntity(Level level, LivingEntity shooter, double velocityX, double velocityY, double velocityZ) {
        this(BEntityTypes.SYRINGE_PROJECTILE.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        this.setDeltaMovement(velocityX, velocityY, velocityZ);
    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public static void playLaunchSound(Level world, Vec3 location, float pitch) {
        AllSoundEvents.FWOOMP.playAt(world, location, 1, pitch, true);
    }

    public static void playHitSound(Level world, Vec3 location) {
        AllSoundEvents.POTATO_HIT.playOnServer(world, BlockPos.containing(location));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();

        if (!level().isClientSide && entity instanceof LivingEntity target && getOwner() instanceof Player player) {
            ItemStack syringeStack = new ItemStack(Items.STICK);
            writeFluid(syringeStack, fluid);

            injectIntoTarget(syringeStack, target, player, level().registryAccess());
            playHitSound(level(), position());
        }

        this.discard();
    }


    @Override
    protected void onHitBlock(BlockHitResult ray) {
        //Vec3 hit = ray.getLocation();

        super.onHitBlock(ray);
        playHitSound(level(), position());
        kill();
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();

        if (!this.isNoGravity()) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, motion.y - 0.05, motion.z);
        }

        if (level().isClientSide) return;

        ItemStack fakeStack = new ItemStack(Items.STICK);
        CompoundTag tag = new CompoundTag();
        fluid.writeToNBT(tag);
        fakeStack.getOrCreateTag().put("Fluid", tag);

        SyringeUtils.super.spawnTrailParticles(level(), this, fakeStack, 5);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        CompoundTag fluidTag = new CompoundTag();
        this.fluid.writeToNBT(fluidTag);
        tag.put("Fluid", fluidTag);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<SyringeProjectileEntity> entityBuilder = (EntityType.Builder<SyringeProjectileEntity>) builder;
        return entityBuilder.sized(.25f, .25f);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        addAdditionalSaveData(tag);
        buffer.writeNbt(tag);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        readAdditionalSaveData(Objects.requireNonNull(buffer.readNbt()));
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }
}
