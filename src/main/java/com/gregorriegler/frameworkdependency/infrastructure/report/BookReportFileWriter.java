package com.gregorriegler.frameworkdependency.infrastructure.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class BookReportFileWriter {
    private static final Logger LOG = LoggerFactory.getLogger(BookReportFileWriter.class);

    public void write(String report) throws IOException {
        Path file = Files.createTempFile("book_report", ".csv");
        Files.writeString(file, report);
        LOG.info("wrote book report to {}", file);
    }
}
