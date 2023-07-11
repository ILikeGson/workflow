package ru.senior.council.workflow.core.resilience;

import lombok.experimental.UtilityClass;
import ru.senior.council.workflow.core.steps.OperationResponse;

import java.util.function.Supplier;

import static ru.senior.council.workflow.core.steps.OperationResultType.FAILED;
import static ru.senior.council.workflow.core.steps.OperationResultType.OK;

@UtilityClass
public class TryCatch {

    public static OperationResponse tryCatch(Supplier<?> supplier) {
        try {
            Object result = supplier.get();

            return new OperationResponse()
                    .type(OK)
                    .source(supplier)
                    .operationResult(result);
        } catch (Exception ex) {
            return new OperationResponse()
                    .type(FAILED)
                    .source(supplier)
                    .errorMessage(ex.getMessage());
        }
    }
}
