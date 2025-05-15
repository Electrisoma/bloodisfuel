package net.electrisoma.bloodisfuel.api.data;

import javax.annotation.Nullable;

public record BurningData(@Nullable Integer durationSeconds,
                          @Nullable Float damagePerSecond)
{}