package com.gregorriegler.frameworkdependency.config;

import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.security.SecurityService;
import com.gregorriegler.frameworkdependency.services.LibraryService;
import com.gregorriegler.frameworkdependency.services.RatingService;
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
}
