package ru.senior.council.workflow.core;

import ru.senior.council.workflow.core.operations.Operation;
import ru.senior.council.workflow.core.operations.OperationProgressReport;
import ru.senior.council.workflow.core.schema.SchemaProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Workflow<O extends Operation> {

    private final SchemaProvider<O> schemaProvider;

    public OperationProgressReport process(O o) {
        return schemaProvider.provideSchema().apply(o);
    }
}
