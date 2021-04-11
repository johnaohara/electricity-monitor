package me.johara.messaging;

import io.micrometer.core.instrument.MeterRegistry;
import me.johara.converter.ElectricityConverter;
import me.johara.converter.UtilityConverter;
import me.johara.converter.WaterConverter;
import me.johara.dto.UtilityTimestamp;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

@Singleton
public class MessageConsumer {

    Logger logger = Logger.getLogger(MessageConsumer.class);

    private final Map<String, UtilityConverter> utilityConverters;


    @Inject
    MessageConsumer(MeterRegistry registry, WaterConverter waterConverter, ElectricityConverter electricityConverter) {
        utilityConverters = new HashMap<>();
        utilityConverters.put(waterConverter.getName(), waterConverter);
        utilityConverters.put(electricityConverter.getName(), electricityConverter);

        waterConverter.registerMetrics(registry);
        electricityConverter.registerMetrics(registry);
        //TODO: Load utility converters via serviceLoader, intializing as managed beans via arc
/*

        try {
            ServiceLoader<UtilityConverter> loader = ServiceLoader.load(UtilityConverter.class);
            loader
                    .stream()
                    .forEach(utilityConverterProvider -> registerUtilityConverter(utilityConverterProvider, registry));
        } catch (ServiceConfigurationError serviceConfigurationError){
            logger.errorf("Unable to load UtilityConverters");
        }
*/
    }

    @Incoming("utility")
    public void process(UtilityTimestamp utilityTimestamp) {
        if (utilityTimestamp != null) {
            if (utilityConverters.containsKey(utilityTimestamp.getUtilityType())) {
                UtilityConverter utilityConverter = utilityConverters.get(utilityTimestamp.getUtilityType());
                logger.debugf("Processing timestamp with UtilityConverter: %s\n", utilityConverter.getName());
                utilityConverter.process(utilityTimestamp);
            } else {
                logger.warnf("No utility converter defined for: %s\n", utilityTimestamp.getUtilityType());
            }
        } else {
            logger.errorf("Utility Timestamp was null");
        }
    }

/*
    private void registerUtilityConverter(ServiceLoader.Provider<UtilityConverter> utilityConverterProvider, MeterRegistry registry) {
        UtilityConverter utilityConverter = utilityConverterProvider.get();
        if(utilityConverter != null && utilityConverter.getName() != null) {
            logger.debugf("Registering UtilityConverter: %s\n", utilityConverter.getName());
            utilityConverter.registerMetrics(registry);
            utilityConverters.put(utilityConverter.getName(), utilityConverter);
        } else {
            logger.warnf("Could not register UtilityConverter: %s\n", utilityConverterProvider.type().toString());
        }
    }
*/
}
