package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.services.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class LibraryReportScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(LibraryReportScheduler.class);

    private final LibraryService libraryService;

    public LibraryReportScheduler(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Scheduled(fixedRate = 25000)
    public void runReport() throws IOException {
        String report = libraryService.generateBookReport();
        File tmpFile = Files.createTempFile("book_report", ".csv").toFile();
        Files.writeString(tmpFile.toPath(), report);
        LOG.info("wrote book report to {}", tmpFile);
    }
}
