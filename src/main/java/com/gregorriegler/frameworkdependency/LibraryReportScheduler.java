package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.model.LibraryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

// MELHORIA 1: o gatilho do agendamento ficou fora do modelo.
// O dominio continua com a regra de gerar o relatorio; a infraestrutura dispara o job.
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
