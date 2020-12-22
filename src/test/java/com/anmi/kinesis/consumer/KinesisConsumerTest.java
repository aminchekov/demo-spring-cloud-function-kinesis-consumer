package com.anmi.kinesis.consumer;

import cloud.localstack.Constants;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.services.kinesis.AmazonKinesisAsync;
import com.anmi.kinesis.consumer.config.EnvironmentHostNameResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.Objects;

import static org.awaitility.Awaitility.await;

@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(
        hostNameResolver = EnvironmentHostNameResolver.class,
        services = "kinesis")
@SpringBootTest(classes = ConsumerApplication.class, properties = {
        "spring.cloud.function.definition=consumeMessages;produceMessage",
        "spring.cloud.stream.bindings.produceMessage-out-0.destination=spring-cloud-stream",
        "spring.cloud.stream.bindings.produceMessage-out-0.producer.header-mode=none",
        "spring.cloud.stream.bindings.produceMessage-out-0.content-type=application/json"
})
@ContextConfiguration(initializers = {KinesisConsumerTest.Initializer.class})
class KinesisConsumerTest {

    @Value("${spring.cloud.stream.bindings.consumeMessages-in-0.destination}")
    private String streamName;
    @Autowired
    private AmazonKinesisAsync kinesisClient;
    @Autowired
    private EntityRepository entityRepository;

    @Test
    public void shouldProcessKinesisMessage() {
        givenThereIsAKinesisStream:
        {
            await().until(() ->
                    kinesisClient.describeStream(streamName).getStreamDescription().getStreamStatus().equals("ACTIVE")
            );
        }

        thenTheConsumerReadsTheMessageFromTheStream:
        {
            await().until(() -> entityRepository.size() > 0 && entityRepository.stream().allMatch(Objects::nonNull));
        }
    }

//    private void produceMessage() {
//        PutRecordRequest putRecordRequest = new PutRecordRequest()
//                .withStreamName(streamName)
//                .withPartitionKey("some_partition_key")
//                .withData(ByteBuffer.wrap("Hello".getBytes()));
//        kinesisClient.putRecord(putRecordRequest);
//    }

    static public class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "cloud.aws.region.static=" + Constants.DEFAULT_REGION
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
