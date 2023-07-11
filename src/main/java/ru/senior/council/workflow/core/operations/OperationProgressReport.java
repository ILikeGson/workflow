package ru.senior.council.workflow.core.operations;

import ru.senior.council.workflow.core.steps.OperationResultType;
import ru.senior.council.workflow.core.steps.ErrorDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true, fluent = true)
public class OperationProgressReport {
    private Operation operation;
    private List<ErrorDetails> errorDetails;
    private OperationResultType resultType;
}