package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.services.LibraryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LibraryReportScheduler {

    private final LibraryService libraryService;

    public LibraryReportScheduler(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Scheduled(fixedRate = 25000)
    public void runReport() throws IOException {
        libraryService.createBookReport();
    }
}
