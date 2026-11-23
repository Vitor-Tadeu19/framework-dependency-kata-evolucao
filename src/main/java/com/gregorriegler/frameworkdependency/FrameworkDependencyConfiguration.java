package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.infrastructure.report.TempFileReportWriter;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.security.SecurityService;
import com.gregorriegler.frameworkdependency.services.BookReportFormatter;
import com.gregorriegler.frameworkdependency.services.BookReportService;
import com.gregorriegler.frameworkdependency.services.LibraryService;
import com.gregorriegler.frameworkdependency.services.RatingService;
import com.gregorriegler.frameworkdependency.services.ReportWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrameworkDependencyConfiguration {

    @Bean
    public SecurityService securityService() {
        return new SecurityService();
    }

    @Bean
    public LibraryService libraryService(SecurityService securityService, BookRepository repository) {
        return new LibraryService(securityService, repository);
    }

    @Bean
    public RatingService ratingService(SecurityService securityService, BookRepository repository) {
        return new RatingService(securityService, repository);
    }

    @Bean
    public BookReportFormatter bookReportFormatter() {
        return new BookReportFormatter();
    }

    @Bean
    public ReportWriter reportWriter() {
        return new TempFileReportWriter();
    }

    @Bean
    public BookReportService bookReportService(BookRepository repository,
                                               BookReportFormatter formatter,
                                               ReportWriter writer) {
        return new BookReportService(repository, formatter, writer);
    }
}