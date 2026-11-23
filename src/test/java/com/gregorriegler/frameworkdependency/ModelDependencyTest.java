package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.model.Rating;
import com.gregorriegler.frameworkdependency.model.User;
import com.gregorriegler.frameworkdependency.model.enums.RatingStars;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.security.SecurityService;
import com.gregorriegler.frameworkdependency.services.LibraryService;
import com.gregorriegler.frameworkdependency.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ModelDependencyTest {

    @Test
    void servicesUseBookRepositoryWithoutSpring() {
        BookRepository repository = mock(BookRepository.class);
        SecurityService securityService = new SecurityService();
        LibraryService library = new LibraryService(securityService, repository);
        RatingService ratings = new RatingService(securityService, repository);
        User user = new User("reader", Set.of("ROLE_USER"));
        Book book = new Book("123", "Title", "Author");
        when(repository.findAll()).thenReturn(List.of(book));
        when(repository.findById("123")).thenReturn(Optional.of(book));

        assertThat(library.getAllBooks(user)).containsExactly(book);
        ratings.rateBook(user, "123", RatingStars.FIVE, "Comment");

        verify(repository).save(book);
        assertThat(book.ratings).singleElement().satisfies(rating -> {
            assertThat(rating.user).isEqualTo("reader");
            assertThat(rating.stars).isEqualTo(RatingStars.FIVE);
            assertThat(rating.comment).isEqualTo("Comment");
        });
    }

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
