package ru.senior.council.workflow;

import ru.senior.council.workflow.core.operations.Operation;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestOperation extends Operation {
    String data;

    Integer counter;
}