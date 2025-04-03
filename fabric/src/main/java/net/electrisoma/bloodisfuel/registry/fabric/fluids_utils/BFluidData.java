package net.electrisoma.bloodisfuel.registry.fabric.fluids_utils;

import com.tterrag.registrate.fabric.FluidData;


@SuppressWarnings("unused")
public record BFluidData(String translationKey, int light) {

    public static class Builder {

        public void translationKey(String key) {
        }

        public Builder luminosity(int light) {
            return this;
        }
    }

    public interface FluidAttributes {
        FluidData getData();

        void setData(FluidData data);
    }
}