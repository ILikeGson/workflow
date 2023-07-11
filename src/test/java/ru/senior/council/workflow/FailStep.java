package ru.senior.council.workflow;

import ru.senior.council.workflow.core.steps.AbstractStep;
import ru.senior.council.workflow.core.steps.StepResult;

public class FailStep extends AbstractStep<TestOperation> {
    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        return StepResult.failed(testOperation, stepName());
    }
}
