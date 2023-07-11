package ru.senior.council.workflow.core.steps;

import java.time.OffsetDateTime;

public record ErrorResponse(String id, String message, OffsetDateTime timestamp) {
}
