package com.gregorriegler.frameworkdependency.services;

import com.gregorriegler.frameworkdependency.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BookReportService {
    private final BookRepository repository;

    public BookReportService(BookRepository repository) {
        this.repository = repository;
    }

    public String generateBookReport() {
        return repository.findReportEntries().stream()
            .map(entry -> csvField(entry.isbn()) + "," + csvField(entry.title()))
            .collect(Collectors.joining("\n"));
    }

    private String csvField(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
