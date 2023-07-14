package ru.senior.council.workflow;

import ru.senior.council.workflow.core.steps.Step;
import ru.senior.council.workflow.core.steps.StepResult;

public class EnrichWithDataStep extends Step<TestOperation> {
    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        testOperation.data("test");
        return StepResult.ok(testOperation, name());
    }
}
