package ru.senior.council.workflow;

import ru.senior.council.workflow.core.steps.Step;
import ru.senior.council.workflow.core.steps.StepResult;

public class DummyStep extends Step<TestOperation> {
    public DummyStep() {
    }

    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        return StepResult.ok(testOperation, this.name());
    }
}