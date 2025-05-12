package net.electrisoma.bloodisfuel.registry.items.syringe_gun;

import com.simibubi.create.AllSoundEvents;
import net.electrisoma.bloodisfuel.api.BurningData;
import net.electrisoma.bloodisfuel.registry.BEntityTypes;
import net.electrisoma.bloodisfuel.registry.items.ItemUtils;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidType;
import net.electrisoma.bloodisfuel.api.equipment.SyringeFluidTypeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class SyringeProjectileEntity extends AbstractHurtingProjectile implements IEntityAdditionalSpawnData, ItemUtils {

    public SyringeProjectileEntity(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    private FluidStack fluid = FluidStack.EMPTY;

    public SyringeProjectileEntity(Level level, LivingEntity shooter) {
        this(BEntityTypes.SYRINGE_PROJECTILE.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        this.setDeltaMovement(shooter.getLookAngle().scale(2.5));
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

        if (!level().isClientSide && entity instanceof LivingEntity target) {
            RegistryAccess access = level().registryAccess();
            SyringeFluidType type = SyringeFluidTypeManager.fromFluid(fluid, access);
            List<MobEffectInstance> effects = SyringeFluidTypeManager.getEffects(type, fluid);

            for (MobEffectInstance effect : effects) {
                target.addEffect(new MobEffectInstance(effect));
            }

            if (type != null && type.hasBurning()) {
                type.burning().ifPresent(burningData -> applyBurningEffect(target, burningData));
            }

            playHitSound(level(), position());
        }

        this.discard();
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

        ItemUtils.super.spawnBloodParticles(level(), this, fakeStack);
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
        readAdditionalSaveData(buffer.readNbt());
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }
}
