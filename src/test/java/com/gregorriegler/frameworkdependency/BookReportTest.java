package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.services.LibraryService;
import com.gregorriegler.frameworkdependency.security.SecurityService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookReportTest {

    @Test
    public void writes_proper_report() {
        BookRepository repository = mock(BookRepository.class);
        when(repository.findAll()).thenReturn(List.of(new Book("123", "title", "irrelevant")));

        LibraryService libraryService = new LibraryService(new SecurityService(), repository);

        assertThat(libraryService.generateBookReport()).isEqualTo("123,title");
    }
}
