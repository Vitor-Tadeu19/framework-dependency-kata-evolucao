package com.gregorriegler.frameworkdependency.services;

import com.gregorriegler.frameworkdependency.model.Book;

import java.util.Collection;
import java.util.stream.Collectors;

public class BookReportFormatter {

    public String format(Collection<Book> books) {
        return books.stream()
                .map(book -> book.isbn + "," + book.title)
                .collect(Collectors.joining("\n"));
    }
}