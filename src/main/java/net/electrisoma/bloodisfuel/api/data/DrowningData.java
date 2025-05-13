package net.electrisoma.bloodisfuel.api.data;

import javax.annotation.Nullable;

public record DrowningData(@Nullable Integer durationSeconds, @Nullable Float damagePerSecond) {}
