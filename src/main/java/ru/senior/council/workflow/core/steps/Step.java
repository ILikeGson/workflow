package ru.senior.council.workflow.core.steps;

import ru.senior.council.workflow.core.operations.Operation;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
public abstract class Step<O extends Operation> {

    private String name;
    private Supplier<FallbackResult> fallback;

    public abstract StepResult<O> apply(O o);
}
