package ru.senior.council.workflow.core.decorators;

import ru.senior.council.workflow.core.steps.Step;
import ru.senior.council.workflow.core.operations.Operation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class StepDecorator<O extends Operation> extends Step<O> {
    protected Step<O> step;

    public StepDecorator(Step<O> step) {
        this.step = step;
        this.fallback(step.fallback());
        this.name(step.name());
    }
}
