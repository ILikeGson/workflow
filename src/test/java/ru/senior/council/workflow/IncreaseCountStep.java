package ru.senior.council.workflow;

import ru.senior.council.workflow.core.steps.Step;
import ru.senior.council.workflow.core.steps.StepResult;

import java.util.Objects;

public class IncreaseCountStep extends Step<TestOperation> {
    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        testOperation.counter(Objects.isNull(testOperation.counter)
                ? 1
                : testOperation.counter + 1
        );
        return StepResult.ok(testOperation, name());
    }
}
