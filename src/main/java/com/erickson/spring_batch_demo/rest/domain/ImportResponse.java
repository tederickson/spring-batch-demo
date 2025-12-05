package com.erickson.spring_batch_demo.rest.domain;

import org.springframework.batch.core.BatchStatus;

public record ImportResponse(BatchStatus status,
                             Integer insertCount,
                             Integer rejectCount,
                             Long duration) {
}
