package ru.senior.council.workflow.core.resilience;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@UtilityClass
public class Try {

    public static <T> T catchIfExThrownAndGetDefault(Supplier<T> supplier, Supplier<T> defaultSup) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return defaultSup.get();
        }
    }
}
