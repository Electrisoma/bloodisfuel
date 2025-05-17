package net.electrisoma.bloodisfuel.api.data;

import javax.annotation.Nullable;


public record FreezingData(@Nullable Integer durationSeconds,
                           @Nullable Float damagePerSecond,
                           @Nullable Float slowAmount)
{}
