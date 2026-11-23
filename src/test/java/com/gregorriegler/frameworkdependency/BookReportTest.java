package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.infrastructure.report.TempFileReportWriter;
import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.services.BookReportFormatter;
import com.gregorriegler.frameworkdependency.services.BookReportService;
import com.gregorriegler.frameworkdependency.services.ReportWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookReportTest {

    @Test
    public void writes_proper_report() throws IOException {
        BookRepository repository = mock(BookRepository.class);
        when(repository.findAll()).thenReturn(List.of(new Book("123", "title", "irrelevant")));
        List<Path> files = new ArrayList<>();
        ReportWriter writer = content -> {
            Path file = new TempFileReportWriter().write(content);
            files.add(file);
            return file;
        };
        BookReportService service = new BookReportService(repository, new BookReportFormatter(), writer);

        service.createBookReport();

        try {
            assertThat(Files.readString(files.get(0))).isEqualTo("123,title");
        } finally {
            Files.deleteIfExists(files.get(0));
        }
    }
}