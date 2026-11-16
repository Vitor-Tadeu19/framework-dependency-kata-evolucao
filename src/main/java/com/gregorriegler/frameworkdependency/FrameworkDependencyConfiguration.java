package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.model.LibraryService;
import com.gregorriegler.frameworkdependency.model.SecurityService;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// MELHORIA 1: a configuracao do Spring fica fora do modelo.
// Aqui o framework monta o objeto do dominio e entrega as dependencias.
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
}
