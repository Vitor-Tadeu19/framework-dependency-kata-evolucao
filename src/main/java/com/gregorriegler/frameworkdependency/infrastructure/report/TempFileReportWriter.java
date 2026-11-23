package com.gregorriegler.frameworkdependency.infrastructure.report;

import com.gregorriegler.frameworkdependency.services.ReportWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempFileReportWriter implements ReportWriter {

    @Override
    public Path write(String content) throws IOException {
        Path file = Files.createTempFile("book_report", ".csv");
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file;
    }
}