package com.anmi.kinesis.consumer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class SimpleEntity {
    private String uuid;
    private String source;

    public SimpleEntity(UUID uuid, String source) {
        this.uuid = uuid.toString();
        this.source = source;
    }
}
