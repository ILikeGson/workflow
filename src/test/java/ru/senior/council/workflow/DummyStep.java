package ru.senior.council.workflow;

import ru.senior.council.workflow.core.steps.AbstractStep;
import ru.senior.council.workflow.core.steps.StepResult;

public class DummyStep extends AbstractStep<TestOperation> {
    public DummyStep() {
    }

    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        return StepResult.ok(testOperation, this.stepName());
    }
}