package ru.senior.council.workflow;

import ru.senior.council.workflow.core.decorators.chains.BaseDecoratorChain;
import ru.senior.council.workflow.core.operations.Operation;
import ru.senior.council.workflow.core.schema.Schema;
import ru.senior.council.workflow.core.schema.SchemaBuilder;
import ru.senior.council.workflow.core.steps.Step;

import org.junit.jupiter.api.Test;
import ru.senior.council.workflow.core.steps.OperationResultType;

import static org.assertj.core.api.Assertions.*;

public class SchemaTest {
    @Test
    void test_schemaWithoutSteps_shouldThrowIllegalArgumentException() {
        Schema<Operation> schema = SchemaBuilder.builder().build();

        assertThatThrownBy(() -> schema.apply(new Operation() {}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Steps were not configured");
    }

    @Test
    void test_schemaWithOnlyOneDummyStep_shouldBeOk() {
        Step<TestOperation> updatePersonStep = new DummyStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(updatePersonStep, "updatePersonStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
                    assertThat(report.errorDetails()).isNull();
                    assertThat(report.resultType()).isEqualTo(OperationResultType.OK);
                });
    }

    @Test
    void test_schemaWithOnlyOneStep_shouldBeOk() {
        Step<TestOperation> enrichWithDataStep = new EnrichWithDataStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(enrichWithDataStep, "enrichWithDataStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.OK);
            assertThat(((TestOperation)report.operation()).data()).isEqualTo("test");
        });
    }

    @Test
    void test_schemaWithTwoSteps_shouldBeOk() {
        Step<TestOperation> enrichWithDataStep = new EnrichWithDataStep();
        Step<TestOperation> increaseCountStep = new IncreaseCountStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(enrichWithDataStep, "enrichWithDataStep")
                .step(increaseCountStep, "increaseCountStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.OK);
            assertThat(((TestOperation)report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation)report.operation()).counter()).isEqualTo(1);
        });
    }

    @Test
    void test_schemaWithThreeStepsAndLastIsFailed_shouldBeFailed() {
        Step<TestOperation> enrichWithDataStep = new EnrichWithDataStep();
        Step<TestOperation> increaseCountStep = new IncreaseCountStep();
        Step<TestOperation> failStep = new FailStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(enrichWithDataStep, "enrichWithDataStep")
                .step(increaseCountStep, "increaseCountStep")
                .step(failStep, "failStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.FAILED);
            assertThat(((TestOperation)report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation)report.operation()).counter()).isEqualTo(1);
        });
    }

    @Test
    void test_schemaWithDecoratorsAndThreeStepsAndLastIsFailed_shouldBeFailed() {
        Step<TestOperation> enrichWithDataStep = new EnrichWithDataStep();
        Step<TestOperation> increaseCountStep = new IncreaseCountStep();
        Step<TestOperation> failStep = new FailStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .withDecoratorChain(new BaseDecoratorChain<TestOperation>())
                .step(enrichWithDataStep, "enrichWithDataStep")
                .step(increaseCountStep, "increaseCountStep")
                .step(failStep, "failStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.FAILED);
            assertThat(((TestOperation)report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation)report.operation()).counter()).isEqualTo(1);
        });
    }
}
