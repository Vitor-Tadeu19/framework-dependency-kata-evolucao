package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.services.BookReportFormatter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookReportFormatterTest {

    private final BookReportFormatter formatter = new BookReportFormatter();

    @Test
    void formats_one_book_as_isbn_and_title() {
        String csv = formatter.format(List.of(new Book("123", "title", "irrelevant")));

        assertThat(csv).isEqualTo("123,title");
    }

    @Test
    void separates_books_with_new_lines() {
        String csv = formatter.format(List.of(
                new Book("1", "first", "a"),
                new Book("2", "second", "b")));

        assertThat(csv).isEqualTo("1,first\n2,second");
    }

    @Test
    void empty_library_produces_empty_report() {
        assertThat(formatter.format(List.of())).isEmpty();
    }
}