package net.electrisoma.bloodisfuel.infrastructure;

import net.electrisoma.bloodisfuel.BloodIsFuel;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.network.chat.MutableComponent;


@SuppressWarnings("unused")
public class Lang extends com.simibubi.create.foundation.utility.Lang {

    public static MutableComponent translateDirect(String key, Object... args) {
        return Components.translatable(BloodIsFuel.MOD_ID + "." + key, resolveBuilders(args));
    }

    public static LangBuilder builder() {
        return new LangBuilder(BloodIsFuel.MOD_ID);
    }
}