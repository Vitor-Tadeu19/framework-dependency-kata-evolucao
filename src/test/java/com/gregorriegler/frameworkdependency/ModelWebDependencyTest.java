package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.services.LibraryService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelWebDependencyTest {

    @Test
    void model_does_not_return_web_response_types() throws NoSuchMethodException {
        Method method = LibraryService.class.getDeclaredMethod("getAllBooks");

        assertThat(method.getReturnType().getName()).isEqualTo("java.util.Collection");
    }
}
