package com.gregorriegler.frameworkdependency.services;

import com.gregorriegler.frameworkdependency.model.AuthorizationException;
import com.gregorriegler.frameworkdependency.model.Book;
import com.gregorriegler.frameworkdependency.model.Rating;
import com.gregorriegler.frameworkdependency.model.RatingStars;
import com.gregorriegler.frameworkdependency.model.User;
import com.gregorriegler.frameworkdependency.repository.BookRepository;
import com.gregorriegler.frameworkdependency.security.SecurityService;

import java.util.Collection;
import java.util.stream.Collectors;

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

    public void rateBook(User user, String isbn, RatingStars stars, String comment) {
        if (!securityService.canRateBook(user)) {
            throw new AuthorizationException("access denied!");
        }
        repository.findById(isbn).ifPresent(book -> {
            Rating rating = new Rating();
            rating.user = user.name();
            rating.stars = stars;
            rating.comment = comment;
            book.ratings.add(rating);
            repository.save(book);
        });
    }

    public String generateBookReport() {
        return repository.findAll().stream()
            .map(book -> book.isbn + "," + book.title)
            .collect(Collectors.joining("\n"));
    }
}
