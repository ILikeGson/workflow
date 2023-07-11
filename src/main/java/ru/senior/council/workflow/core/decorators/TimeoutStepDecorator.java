package ru.senior.council.workflow.core.decorators;

import ru.senior.council.workflow.core.operations.Operation;
import ru.senior.council.workflow.core.steps.AbstractStep;
import ru.senior.council.workflow.core.steps.StepResult;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeoutStepDecorator<O extends Operation> extends StepDecorator<O> {
    private final Duration timeout;

    public TimeoutStepDecorator(AbstractStep<O> step, Duration timeout) {
        super(step);
        this.timeout = timeout;
    }

    @Override
    public StepResult<O> apply(O o) {
        LocalDateTime startTime = LocalDateTime.now();

        StepResult<O> result = step.apply(o);

        LocalDateTime endTime = LocalDateTime.now();

        if (result.isFailed()) {
            return result;
        }

        Duration difference = Duration.between(startTime, endTime);

        return timeout.minus(difference).toNanos() < 0
                ? StepResult.failed(o, step.stepName())
                : StepResult.ok(o, step.stepName());
    }
}
