package net.electrisoma.bloodisfuel.api.data;

import javax.annotation.Nullable;

public record ExtinguishingData(@Nullable Integer durationSeconds, @Nullable Float healPerSecond) {}
