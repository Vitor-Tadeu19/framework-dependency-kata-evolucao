package com.gregorriegler.frameworkdependency.scheduler;

import com.gregorriegler.frameworkdependency.infrastructure.report.BookReportFileWriter;
import com.gregorriegler.frameworkdependency.services.BookReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LibraryReportScheduler {
    private final BookReportService reportService;
    private final BookReportFileWriter writer;

    public LibraryReportScheduler(BookReportService reportService, BookReportFileWriter writer) {
        this.reportService = reportService;
        this.writer = writer;
    }

    @Scheduled(fixedRate = 25000)
    public void runReport() throws IOException {
        writer.write(reportService.generateBookReport());
    }
}
