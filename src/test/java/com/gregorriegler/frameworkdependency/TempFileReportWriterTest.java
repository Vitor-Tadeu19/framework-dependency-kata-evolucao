package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.infrastructure.report.TempFileReportWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class TempFileReportWriterTest {

    @Test
    void writes_content_to_a_book_report_csv_file() throws IOException {
        Path file = new TempFileReportWriter().write("123,title");

        try {
            assertThat(file.getFileName().toString()).startsWith("book_report").endsWith(".csv");
            assertThat(Files.readString(file)).isEqualTo("123,title");
        } finally {
            Files.deleteIfExists(file);
        }
    }
}