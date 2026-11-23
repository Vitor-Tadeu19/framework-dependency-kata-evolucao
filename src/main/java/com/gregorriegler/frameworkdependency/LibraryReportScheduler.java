package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.services.BookReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LibraryReportScheduler {

    private final BookReportService bookReportService;

    public LibraryReportScheduler(BookReportService bookReportService) {
        this.bookReportService = bookReportService;
    }

    @Scheduled(fixedRate = 25000)
    public void runReport() throws IOException {
        bookReportService.createBookReport();
    }
}