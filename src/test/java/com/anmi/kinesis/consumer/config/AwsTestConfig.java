package com.anmi.kinesis.consumer.config;

import cloud.localstack.Constants;
import cloud.localstack.Localstack;
import cloud.localstack.awssdkv1.TestUtils;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisAsync;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextResourceLoaderAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
@EnableAutoConfiguration(exclude = {
        ContextResourceLoaderAutoConfiguration.class,
        ContextStackAutoConfiguration.class
})
public class AwsTestConfig {

    @Bean
    @Primary
    public AWSCredentialsProvider awsCredentialsProvider() {
        return TestUtils.getCredentialsProvider();
    }

    @Bean
    @Primary
    public AmazonKinesisAsync kinesisClientAsync() {
        return TestUtils.getClientKinesisAsync();
    }

    @Bean
    @Primary
    public AmazonKinesis kinesisClient() {
        return TestUtils.getClientKinesis();
    }

    @Bean
    @Primary
    public AmazonDynamoDB dynamoDBClient() {
        return TestUtils.getClientDynamoDB();
    }

    @Bean
    @Primary
    public AmazonDynamoDBAsync amazonDynamoDBAsync(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonDynamoDBAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(Localstack.INSTANCE.getEndpointDynamoDB(), Constants.DEFAULT_REGION))
                .withCredentials(awsCredentialsProvider)
                .build();
    }

    @Bean
    @Primary
    public AmazonDynamoDBStreams amazonDynamoDBStreams() {
        return TestUtils.getClientDynamoDBStreams();
    }

    @Bean
    public MessageConverter providesJsonMessageConverter() {
        return new MappingJackson2MessageConverter();
    }
}
