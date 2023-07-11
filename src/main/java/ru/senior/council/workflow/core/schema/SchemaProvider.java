package ru.senior.council.workflow.core.schema;

import ru.senior.council.workflow.core.operations.Operation;

public interface SchemaProvider<O extends Operation> {
    Schema<O> provideSchema();
}
