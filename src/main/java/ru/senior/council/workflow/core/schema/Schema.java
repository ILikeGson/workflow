package ru.senior.council.workflow.core.schema;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import ru.senior.council.workflow.core.operations.Operation;
import ru.senior.council.workflow.core.operations.OperationProgressReport;
import ru.senior.council.workflow.core.resilience.Retry;
import ru.senior.council.workflow.core.resilience.Try;
import ru.senior.council.workflow.core.steps.ErrorDetails;
import ru.senior.council.workflow.core.steps.FallbackResult;
import ru.senior.council.workflow.core.steps.Step;
import ru.senior.council.workflow.core.steps.StepResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static ru.senior.council.workflow.core.steps.OperationResultType.FAILED;
import static ru.senior.council.workflow.core.steps.OperationResultType.OK;

@RequiredArgsConstructor
public class Schema<O extends Operation> {
    private static final String DEFAULT_ERROR_MESSAGE = "Step '%s' was not rollbacked";

    private final Retry retry;
    private final List<Step<O>> steps;


    public OperationProgressReport apply(O o) {
        if (CollectionUtils.isEmpty(steps)) {
            throw new IllegalArgumentException("Steps were not configured");
        }

        ListIterator<Step<O>> iterator = steps.listIterator();
        OperationProgressReport report = new OperationProgressReport();

        while (iterator.hasNext()) {
            Step<O> step = iterator.next();

            StepResult<O> result = Try.catchIfExThrownAndGetDefault(
                    () -> step.apply(o),
                    () -> StepResult.failed(o, step.name())
            );

            if (result.isFailed()) {
                if (nonNull(retry) && tryRetry(o, step).isOk()) {
                    continue;
                }

                if (nonNull(step.fallback())) {
                    List<ErrorDetails> errorDetails = rollback(iterator);
                    if (isNotEmpty(errorDetails)) {
                        return report.operation(o).resultType(FAILED).errorDetails(errorDetails);
                    }
                } else {
                    return report.operation(o).resultType(FAILED);
                }
            }
        }

        return report.resultType(OK).operation(o);
    }

    private StepResult<O> tryRetry(O o, Step<O> step) {
        return nonNull(retry.backoff())
                ? ForkJoinPool.commonPool()
                        .submit(() -> retryProcess(o, step))
                        .join()
                : retryProcess(o, step);
    }

    @SneakyThrows
    private StepResult<O> retryProcess(O o, Step<O> step) {
        int retryAttempts = retry.maximumAttempts();
        while (retryAttempts > 0) {
            waitIfBackoffWasSetup();

            StepResult<O> retryResult = step.apply(o);
            if (retryResult.isOk()) {
                return retryResult;
            }
            retryAttempts--;
        }

        return StepResult.failed(o, step.name());
    }

    private void waitIfBackoffWasSetup() throws InterruptedException {
        if (nonNull(retry.backoff())) {
            TimeUnit.NANOSECONDS.wait(retry.backoff().getDelayInNanos());
        }
    }

    private List<ErrorDetails> rollback(ListIterator<Step<O>> iterator) {
        List<ErrorDetails> details = new ArrayList<>();
        while (iterator.hasPrevious()) {
            Step<? extends O> step = iterator.previous();
            FallbackResult fallbackResult = Try.catchIfExThrownAndGetDefault(
                step.fallback(),
                () -> FallbackResult.FAILED
            );

            if (fallbackResult == FallbackResult.FAILED) {
                details.add(
                    new ErrorDetails(
                        step.name(),
                        DEFAULT_ERROR_MESSAGE.formatted(step.name())
                    )
                );
            }
        }

        return details;
    }
}
