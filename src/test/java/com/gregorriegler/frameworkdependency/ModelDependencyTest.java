package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.model.LibraryService;
import com.gregorriegler.frameworkdependency.model.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelDependencyTest {

    @Test
    void model_classes_do_not_have_spring_service_annotations() {
        assertThat(LibraryService.class.getAnnotation(Service.class)).isNull();
        assertThat(SecurityService.class.getAnnotation(Service.class)).isNull();
    }
}
