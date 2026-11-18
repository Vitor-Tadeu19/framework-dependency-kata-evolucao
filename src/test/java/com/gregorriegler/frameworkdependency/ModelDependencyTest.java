package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.model.Rating;
import com.gregorriegler.frameworkdependency.security.SecurityService;
import com.gregorriegler.frameworkdependency.services.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelDependencyTest {

    @Test
    void model_classes_do_not_have_spring_service_annotations() {
        assertThat(LibraryService.class.getAnnotation(Service.class)).isNull();
        assertThat(SecurityService.class.getAnnotation(Service.class)).isNull();
    }

    @Test
    void domain_entities_do_not_have_persistence_annotations() {
        assertThat(Book.class.getAnnotations()).isEmpty();
        assertThat(Rating.class.getAnnotations()).isEmpty();
    }
}
