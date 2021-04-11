package me.johara.converter;

import io.micrometer.core.instrument.MeterRegistry;
import me.johara.dto.UtilityTimestamp;

public interface UtilityConverter<T> {

    void process(UtilityTimestamp utilityTimestamp);

    String getName();

    void registerMetrics(MeterRegistry registry);
}
