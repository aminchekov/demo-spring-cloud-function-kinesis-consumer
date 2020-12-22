package com.anmi.kinesis.consumer;

import lombok.experimental.Delegate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EntityRepository {
    @Delegate
    private final List<SimpleEntity> delegate = new ArrayList<>();
}
