package com.anmi.kinesis.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;

@SpringBootApplication
@Slf4j
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public Consumer<Message<List<SimpleEntity>>> consumeMessages(EntityRepository entityRepository) {
        return messages -> {
            log.info("Messages are consumed as {}", messages.getPayload());
            entityRepository.addAll(messages.getPayload());
        };
    }
}
