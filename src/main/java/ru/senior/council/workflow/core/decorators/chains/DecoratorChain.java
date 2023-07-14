package ru.senior.council.workflow.core.decorators.chains;

import ru.senior.council.workflow.core.steps.Step;
import ru.senior.council.workflow.core.operations.Operation;
import ru.senior.council.workflow.core.decorators.StepDecorator;

public interface DecoratorChain<O extends Operation> {

    Step<O> decorate(Step<O> step);
}