package ru.senior.council.workflow.core.decorators.chains;

import ru.senior.council.workflow.core.steps.AbstractStep;
import ru.senior.council.workflow.core.operations.Operation;
import ru.senior.council.workflow.core.decorators.CatchExceptionIfThrownDecorator;
import ru.senior.council.workflow.core.decorators.LogStepDecorator;
import ru.senior.council.workflow.core.decorators.StepDecorator;
import ru.senior.council.workflow.core.decorators.TimeTrackingStepDecorator;

public class BaseDecoratorChain<O extends Operation> implements DecoratorChain<O>{

    public StepDecorator<O> decorate(AbstractStep<O> step) {
        return
                new LogStepDecorator<>(
                    new TimeTrackingStepDecorator<>(
                        new CatchExceptionIfThrownDecorator<>(step)
                    )
                );
    }
}
