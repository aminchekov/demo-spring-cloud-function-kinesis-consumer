package com.anmi.kinesis.consumer.config;

import com.anmi.kinesis.consumer.SimpleEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Supplier;

@Configuration
public class SupplierTestConfig {

    @Bean
    public Supplier<SimpleEntity> produceMessage() {
        return () -> new SimpleEntity(UUID.randomUUID(), "source");
    }
}
