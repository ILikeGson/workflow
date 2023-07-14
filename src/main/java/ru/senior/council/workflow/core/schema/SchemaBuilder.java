package ru.senior.council.workflow.core.schema;

import ru.senior.council.workflow.core.decorators.chains.DecoratorChain;
import ru.senior.council.workflow.core.operations.Operation;
import ru.senior.council.workflow.core.resilience.Retry;
import ru.senior.council.workflow.core.steps.Step;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class SchemaBuilder<O extends Operation> {
    private final List<Step<O>> steps = new ArrayList<>();

    private Retry retry;
    private DecoratorChain<O> decoratorChain;

    public static <O extends Operation> SchemaBuilder<O> builder() {
        return new SchemaBuilder<O>();
    }

    public SchemaBuilder<O> withDecoratorChain(DecoratorChain<? extends O> decoratorChain) {
        this.decoratorChain = (DecoratorChain<O>) decoratorChain;
        return this;
    }

    public SchemaBuilder<O> step(Step<? extends O> step) {
        steps.add((Step<O>) step);
        return this;
    }

    public SchemaBuilder<O> step(Step<? extends O> step, String stepName) {
        step.name(stepName);
        steps.add((Step<O>) step);
        return this;
    }

    public SchemaBuilder<O> withRetry(Retry retry) {
        this.retry = retry;
        return this;
    }

    public Schema<O> build() {
        return nonNull(decoratorChain)
                ? new Schema<>(retry, steps.stream().map(step -> decoratorChain.decorate(step)).toList())
                : new Schema<>(retry, steps);
    }
}