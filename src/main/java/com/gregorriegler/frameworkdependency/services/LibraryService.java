package com.gregorriegler.frameworkdependency.services;

import com.gregorriegler.frameworkdependency.model.AuthorizationException;
import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.model.User;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.security.SecurityService;

import java.util.Collection;

public class LibraryService {

    private final SecurityService securityService;
    private final BookRepository repository;

    public LibraryService(SecurityService securityService, BookRepository repository) {
        this.securityService = securityService;
        this.repository = repository;
    }

    public Collection<Book> getAllBooks() {
        throw new AuthorizationException("access denied!");
    }

    public Collection<Book> getAllBooks(User user) {
        if (!securityService.canViewLibrary(user)) {
            throw new AuthorizationException("access denied!");
        }
        return repository.findAll();
    }

    public void createBook(User user, Book book) {
        if (!securityService.canCreateBook(user)) {
            throw new AuthorizationException("access denied!");
        }
        Book persistedBook = repository.findById(book.isbn).orElseGet(Book::new);
        persistedBook.isbn = book.isbn;
        persistedBook.title = book.title;
        persistedBook.author = book.author;
        repository.save(persistedBook);
    }

    public String generateBookReport() {
        return new BookReportFormatter().format(repository.findAll());
    }
}
