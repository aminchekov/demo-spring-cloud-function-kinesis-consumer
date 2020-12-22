package com.anmi.kinesis.consumer.config;

import cloud.localstack.docker.annotation.IHostNameResolver;
import com.amazonaws.SDKGlobalConfiguration;

public class EnvironmentHostNameResolver implements IHostNameResolver {

    public static final String DOCKER_HOST_NAME = "DOCKER_HOST_NAME";

    static {
        System.setProperty(SDKGlobalConfiguration.AWS_CBOR_DISABLE_SYSTEM_PROPERTY, "true");
    }

    @Override
    public String getHostName() {
        return System.getenv(DOCKER_HOST_NAME);
    }

}