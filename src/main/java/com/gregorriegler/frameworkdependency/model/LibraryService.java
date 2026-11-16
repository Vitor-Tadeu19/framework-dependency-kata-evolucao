package com.gregorriegler.frameworkdependency.model;

import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.web.CreateBookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

// MELHORIA 1: classe de dominio sem anotacao do Spring.
// Agora a dependencia de infraestrutura eh injetada via construtor.
public class LibraryService {

    private static final Logger LOG = LoggerFactory.getLogger(LibraryService.class);

    private final SecurityService securityService;
    private final BookRepository repository;

    public LibraryService(SecurityService securityService, BookRepository repository) {
        this.securityService = securityService;
        this.repository = repository;
    }

    public ResponseEntity<Collection<Book>> getAllBooks() {
        if (securityService.isAuthenticated() && (securityService.isAdmin() || securityService.isUser())) {
            return ResponseEntity.ok(repository.findAll());
        } else {
            throw new AccessDeniedException("access denied!");
        }
    }

    public void createBook(CreateBookRequest request) {
        Book book = repository.findById(request.isbn).orElseGet(Book::new);
        book.isbn = request.isbn;
        book.title = request.title;
        book.author = request.author;
        repository.save(book);
    }

    public void createBookReport() throws IOException {
        String report = repository.findAll().stream()
            .map(book -> book.isbn + "," + book.title)
            .collect(Collectors.joining("\n"));
        File tmpFile = File.createTempFile("book_report", ".csv");
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(report);
        writer.close();
        LOG.info("wrote book report to {}", tmpFile.toString());
    }
}
