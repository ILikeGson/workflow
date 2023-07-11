package ru.senior.council.workflow;

import ru.senior.council.workflow.core.steps.AbstractStep;
import ru.senior.council.workflow.core.steps.StepResult;

public class EnrichWithDataStep extends AbstractStep<TestOperation> {
    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        testOperation.data("test");
        return StepResult.ok(testOperation, stepName());
    }
}
