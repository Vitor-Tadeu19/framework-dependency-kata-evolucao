package com.gregorriegler.frameworkdependency.services;

import com.gregorriegler.frameworkdependency.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class BookReportService {

    private static final Logger LOG = LoggerFactory.getLogger(BookReportService.class);

    private final BookRepository repository;
    private final BookReportFormatter formatter;
    private final ReportWriter writer;

    public BookReportService(BookRepository repository, BookReportFormatter formatter, ReportWriter writer) {
        this.repository = repository;
        this.formatter = formatter;
        this.writer = writer;
    }

    public void createBookReport() throws IOException {
        String report = formatter.format(repository.findAll());
        Path file = writer.write(report);
        LOG.info("wrote book report to {}", file);
    }
}