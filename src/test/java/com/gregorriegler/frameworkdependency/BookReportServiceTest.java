package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.services.BookReportFormatter;
import com.gregorriegler.frameworkdependency.services.BookReportService;
import com.gregorriegler.frameworkdependency.services.ReportWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookReportServiceTest {

    private final BookRepository repository = mock(BookRepository.class);

    @Test
    void sends_the_csv_of_all_books_to_the_writer() throws IOException {
        when(repository.findAll()).thenReturn(List.of(new Book("123", "title", "irrelevant")));
        List<String> written = new ArrayList<>();
        BookReportService service = new BookReportService(repository, new BookReportFormatter(), content -> {
            written.add(content);
            return Path.of("fake.csv");
        });

        service.createBookReport();

        assertThat(written).containsExactly("123,title");
    }

    @Test
    void propagates_writer_failures() {
        ReportWriter failing = content -> {
            throw new IOException("disk full");
        };
        BookReportService service = new BookReportService(repository, new BookReportFormatter(), failing);

        assertThatThrownBy(service::createBookReport)
                .isInstanceOf(IOException.class)
                .hasMessage("disk full");
    }
}