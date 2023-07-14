package ru.senior.council.workflow;

import ru.senior.council.workflow.core.steps.Step;
import ru.senior.council.workflow.core.steps.StepResult;

public class FailStep extends Step<TestOperation> {
    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        return StepResult.failed(testOperation, name());
    }
}
