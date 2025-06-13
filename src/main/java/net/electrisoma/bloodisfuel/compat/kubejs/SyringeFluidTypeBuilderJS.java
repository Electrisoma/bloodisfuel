package net.electrisoma.bloodisfuel.compat.kubejs;

import dev.latvian.mods.kubejs.typings.Info;
import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;

import java.util.function.Consumer;

public class SyringeFluidTypeBuilderJS {
    private final SyringeFluidType.Builder builder = new SyringeFluidType.Builder();
    private final Consumer<SyringeFluidType> onBuild;

    public SyringeFluidTypeBuilderJS(Consumer<SyringeFluidType> onBuild) {
        this.onBuild = onBuild;
    }

    @Info("Set fluids this type applies to")
    public SyringeFluidTypeBuilderJS fluids(String... fluidIds) {
        builder.fluids(fluidIds);
        return this;
    }

    @Info("Set fluid appearance (color, opaque, glowing optional)")
    public SyringeFluidTypeBuilderJS appearance(int color, boolean opaque) {
        builder.appearance(color, opaque);
        return this;
    }

    @Info("Set fluid appearance (color, opaque, glowing)")
    public SyringeFluidTypeBuilderJS appearance(int color, boolean opaque, boolean glowing) {
        builder.appearance(color, opaque, glowing);
        return this;
    }

    @Info("Set base damage")
    public SyringeFluidTypeBuilderJS damage(float amount) {
        builder.damage(amount);
        return this;
    }

    @Info("Set attack speed modifier")
    public SyringeFluidTypeBuilderJS attackSpeed(float speed) {
        builder.attackSpeed(speed);
        return this;
    }

    @Info("Set both damage and attack speed")
    public SyringeFluidTypeBuilderJS stats(float damage, float attackSpeed) {
        builder.stats(damage, attackSpeed);
        return this;
    }

    @Info("Set food values")
    public SyringeFluidTypeBuilderJS food(int nutrition, float saturation) {
        builder.food(nutrition, saturation);
        return this;
    }

    @Info("Add a mob effect (effect id, duration ticks, amplifier)")
    public SyringeFluidTypeBuilderJS addEffect(String id, int duration, int amplifier) {
        builder.addEffect(id, duration, amplifier);
        return this;
    }

    @Info("Set burning data (duration in seconds, damage per second)")
    public SyringeFluidTypeBuilderJS burning(int durationSeconds, float damagePerSecond) {
        builder.burning(durationSeconds, damagePerSecond);
        return this;
    }

    @Info("Set extinguishing data (duration in seconds, heal per second)")
    public SyringeFluidTypeBuilderJS extinguishing(int durationSeconds, float healPerSecond) {
        builder.extinguishing(durationSeconds, healPerSecond);
        return this;
    }

    @Info("Set drowning data (duration in seconds, damage per second)")
    public SyringeFluidTypeBuilderJS drowning(int durationSeconds, float damagePerSecond) {
        builder.drowning(durationSeconds, damagePerSecond);
        return this;
    }

    @Info("Set freezing data (duration in seconds, damage per second, slow amount)")
    public SyringeFluidTypeBuilderJS freezing(int durationSeconds, float damagePerSecond, float slowAmount) {
        builder.freezing(durationSeconds, damagePerSecond, slowAmount);
        return this;
    }

    // Finalize the builder and push to event
    public void build() {
        onBuild.accept(builder.build());
    }
}
