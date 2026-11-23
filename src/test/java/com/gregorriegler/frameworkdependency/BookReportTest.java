package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.infrastructure.persistence.SpringDataBookRepository;
import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.model.User;
import com.gregorriegler.frameworkdependency.model.enums.RatingStars;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.services.LibraryService;
import com.gregorriegler.frameworkdependency.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookReportTest {
    private static final User MANAGER = new User("manager", Set.of("ROLE_ADMIN", "ROLE_USER"));

    @Autowired
    private BookRepository repository;

    @Autowired
    private SpringDataBookRepository springDataBookRepository;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private RatingService ratingService;

    @BeforeEach
    void clearBooks() {
        springDataBookRepository.deleteAll();
    }

    @Test
    void generatesCsvForPersistedBookOutsideHttp() {
        libraryService.createBook(MANAGER, new Book("123", "title", "Author"));
        ratingService.rateBook(MANAGER, "123", RatingStars.FIVE, "First");

        assertThat(repository.findById("123").orElseThrow().ratings).hasSize(1);
        assertThat(repository.findAll()).singleElement().satisfies(book ->
            assertThat(book.ratings).hasSize(1));
        assertThat(libraryService.generateBookReport()).isEqualTo("123,title");
    }

    @Test
    void updatingTitlePreservesBothRatingIds() {
        libraryService.createBook(MANAGER, new Book("123", "Original", "Author"));
        ratingService.rateBook(MANAGER, "123", RatingStars.FIVE, "First");
        ratingService.rateBook(MANAGER, "123", RatingStars.FOUR, "Second");
        List<Long> ids = repository.findById("123").orElseThrow().ratings.stream()
            .map(rating -> rating.id)
            .toList();
        assertThat(ids).hasSize(2).doesNotContainNull().doesNotHaveDuplicates();

        libraryService.createBook(MANAGER, new Book("123", "Updated", "Author"));

        assertThat(libraryService.getAllBooks(MANAGER)).singleElement().satisfies(book -> {
            assertThat(book.title).isEqualTo("Updated");
            assertThat(book.ratings).extracting(rating -> rating.id)
                .containsExactlyInAnyOrderElementsOf(ids);
            assertThat(book.ratings).extracting(rating -> rating.comment)
                .containsExactlyInAnyOrder("First", "Second");
        });
    }
}
